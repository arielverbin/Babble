package com.example.babble.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.babble.API.UserAPI;
import com.example.babble.AppDB;
import com.example.babble.R;
import com.example.babble.databinding.ActivitySettingsBinding;
import com.example.babble.serverObjects.UserDataToSettings;
import com.example.babble.entities.MessageDao;
import com.example.babble.entities.ContactDao;
import com.example.babble.entities.Preference;
import com.example.babble.entities.PreferenceDao;
import com.example.babble.utilities.RequestCallBack;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;

    AppDB db;
    PreferenceDao preferenceDao;

    private static final int RESULT_LOGOUT = 1;
    private String base64Image;
    private String base64DataUri;
    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set actionbar title.
        setTitle("Settings");
        if (getSupportActionBar() != null)   //null check
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get instance of preferenceDao.
        this.db = Room.databaseBuilder(SettingsActivity.this, AppDB.class, "AppDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        preferenceDao = this.db.preferenceDao();

        TextView logOutBtn = binding.logOutBtn;
        logOutBtn.setOnClickListener(view -> handleLogOut());

        // display current preferences.
        displayPreferences();

        // image selection.
        ImageView selectPhotoButton = binding.profileImage;
        selectPhotoButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });

        // save button
        Button saveBtn = binding.saveButton;
        saveBtn.setOnClickListener(view -> handleSave());

        // switch theme (light, dark).
        SwitchCompat themeSwitch = binding.themeSwitch;
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        // set according to current theme from device.
        themeSwitch.setChecked(currentNightMode == Configuration.UI_MODE_NIGHT_YES);
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            // Photo selected successfully, handle the selected photo URI
            Uri photoUri = data.getData();

            // Save the photoUri and display it in an ImageView
            saveAndDisplayImage(photoUri);
        }
    }

    private void displayPreferences() {
        EditText displayName = binding.displayNameEditText;
        // display name
        displayName.setText(preferenceDao.get("displayName"));

        // profile picture
        ImageView profilePic = binding.profileImage;

        // set contact picture
        String base64ProfilePic = preferenceDao.get("profilePic");
        // remove "data:image/jpg;base64"
        String pureBase64Encoded = base64ProfilePic.substring(base64ProfilePic.indexOf(",") + 1);
        // decode to bitmap.
        byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        profilePic.setImageBitmap(decodedByte);


        // server url
        EditText serverUrlInput = binding.serverAddressEditText;
        serverUrlInput.setText(preferenceDao.get("serverUrl"));
    }

    private void saveAndDisplayImage(Uri photoUri) {
        try {
            // Convert the image to a byte array
            InputStream inputStream = getContentResolver().openInputStream(photoUri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            inputStream.close();

            // Encode the image byte array to Base64
            this.base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            String mimeType = getContentResolver().getType(photoUri);
            this.base64DataUri = "data:" + mimeType + ";base64," + this.base64Image;

            // Display the photo in an ImageView
            ImageView photoPreview = binding.profileImage; // Replace with your ImageView's ID
            photoPreview.setImageURI(photoUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleLogOut() {
        // clear all dao:
        // the user wouldn't want their data still on their
        // phone even though they logged out.
        ContactDao contactDao = db.contactDao();
        contactDao.clear();
        MessageDao messageDao = db.messageDao();
        messageDao.clearAll();

        // clear user details and token.
        preferenceDao.clear();
        // Set the result code to indicate a successful logout

        setResult(RESULT_LOGOUT);
        finish();
    }


    private void handleSave() {

        if(handleServerUrlChanged()){
            return;
        }

        EditText nameInput = binding.displayNameEditText;
        String newName = nameInput.getText().toString();
        // check if either display name or profile picture changed.

        // create a new userData to send to server.
        // initial state: both modifications are "null"
        UserDataToSettings data = new UserDataToSettings(null, null);

        if (((!newName.isEmpty())
                && (!newName.equals(preferenceDao.get("displayName"))))) {
            data.setNewDisplayName(newName);
        }

        if ((base64Image != null) && (!base64Image.isEmpty())
                && (!base64Image.equals(preferenceDao.get("profilePic")))) {
            data.setNewPic(base64DataUri);
        }

        // at least one of the modifications is not null.
        if ((data.getNewPic() != null) || (data.getNewDisplayName() != null)) {
            UserAPI userAPI = new UserAPI(SettingsActivity.this);
            userAPI.setUserDetails(SettingsActivity.this, data, new RequestCallBack() {
                // fail to save the data.
                @Override
                public void onFailure(String error) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    builder.setTitle("Error Updating data");
                    builder.setMessage(error);
                    builder.setPositiveButton(SettingsActivity.this.getString(R.string.ok), (dialog, which) -> dialog.dismiss());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                // success! update dao.
                @Override
                public void onSuccess() {
                    if (((!newName.isEmpty())
                            && (!newName.equals(preferenceDao.get("displayName"))))) {

                        preferenceDao.set(new Preference("displayName", newName));
                    }
                    if ((base64Image != null) && (!base64Image.isEmpty())
                            && (!base64Image.equals(preferenceDao.get("profilePic")))) {
                        preferenceDao.set(new Preference("profilePic", base64Image));
                    }
                }
            });
        }

        setResult(0);
        finish();

    }

    private boolean handleServerUrlChanged(){
        EditText urlInput = binding.serverAddressEditText;
        String newServerUrl = urlInput.getText().toString();

        // server url was changed.
        if ((!newServerUrl.isEmpty()) && (!newServerUrl.equals(preferenceDao.get("serverUrl")))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
            builder.setTitle("Change Server Address");
            builder.setMessage("This action will log you out, and abort changes in your account (if you made any).");
            builder.setPositiveButton(SettingsActivity.this.getString(R.string.ok), (dialog, which) -> {
                // Perform your desired action after the "OK" button is pressed
                // This code block will execute when the "OK" button is clicked
                // You can add your logic here or call a method
                preferenceDao.set(new Preference("serverUrl", newServerUrl));
                handleLogOut();
                dialog.dismiss();

                setResult(0);
                finish();
            });
            builder.setNegativeButton(SettingsActivity.this.getString(R.string.cancel), (dialog, which) -> {
                dialog.dismiss();
                setResult(0);
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        }

        return false;
    }
}