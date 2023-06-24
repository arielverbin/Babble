package com.example.babble.activities;

import static android.webkit.URLUtil.isValidUrl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.example.babble.API.UserAPI;
import com.example.babble.AppDB;
import com.example.babble.R;
import com.example.babble.databinding.ActivityRegisterBinding;
import com.example.babble.entities.Preference;
import com.example.babble.entities.PreferenceDao;
import com.example.babble.utilities.RequestCallBack;
import com.example.babble.serverObjects.User;

public class RegisterActivity extends AppCompatActivity {
    private String base64DataUri;
    private PreferenceDao preferenceDao;
    private ActivityRegisterBinding binding;
    private static final int PICK_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // create a preference dao instance.
        AppDB db = Room.databaseBuilder(RegisterActivity.this, AppDB.class, "AppDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        preferenceDao = db.preferenceDao();

        // initialize the text in the server url input.
        EditText serverUrlEdit = binding.serverUrlInput;
        if(preferenceDao.get("serverUrl") == null) { //initial entrance to the app!
            serverUrlEdit.setText(RegisterActivity.this.getString(R.string.BaseUrl));
        } else {
            serverUrlEdit.setText(preferenceDao.get("serverUrl"));
        }


        // Disable the actionbar.
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        // add filer to username input
        EditText usernameInput = binding.usernameInput;
        usernameInput.setFilters(new InputFilter[]{new UsernameFilter()});

        TextView loginLink = binding.loginLink;

        //'already registered' button.
        loginLink.setOnClickListener(v -> {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        });
        // photo selection button.
        Button selectPhotoButton = binding.selectPhotoButton;
        selectPhotoButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });

        // register button.
        Button btnRegister = binding.btnRegister;
        btnRegister.setOnClickListener(v -> handleRegister());
    }


    private void handleRegister() {
        TextView errors = binding.errors;
        CardView errorCard = binding.errorCard;

        String username = binding.usernameInput.getText().toString();
        String displayName = binding.nameInput.getText().toString();
        String password = binding.passwordInput.getText().toString();
        String confirmPassword = binding.confirmPasswordInput.getText().toString();
        Drawable photoPreview = binding.photoPreview.getDrawable();
        String serverUrlInput = binding.serverUrlInput.getText().toString();

        if(isValidUrl(serverUrlInput)) {
            preferenceDao.set(new Preference("serverUrl", serverUrlInput));
        } else {
            errorCard.setVisibility(View.VISIBLE);
            errors.setText(R.string.invalid_server_address);
        }

        String errorMsg = produceErrorMsg(displayName, username,
                password, confirmPassword,photoPreview);

        if (!errorMsg.isEmpty()) {
            errorCard.setVisibility(View.VISIBLE);
            errors.setText(errorMsg);
        } else {
            User user = new User(username, displayName,
                    password, this.base64DataUri);
            UserAPI userAPI = new UserAPI(RegisterActivity.this);
            userAPI.post(user, RegisterActivity.this, new RequestCallBack() {

                // fail signing up - display error message.
                @Override
                public void onFailure(String error) {
                    TextView errors = binding.errors;
                    CardView errorCard = binding.errorCard;
                    errorCard.setVisibility(View.VISIBLE);
                    errors.setText(error);
                }

                // success! starting the contacts activity.
                @Override
                public void onSuccess() {
                    Intent intent = new Intent(RegisterActivity.this, ContactsActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
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

    private String produceErrorMsg(String displayName, String username, String password,
                                   String confirmPassword, Drawable pic) {
        if (username.isEmpty() || displayName.isEmpty() || password.isEmpty() ||
                confirmPassword.isEmpty() || pic == null) {
            return "All fields are required";
        } else if (password.length() < 8) {
            return "Password must be at least 8 characters long";
        } else if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one digit";
        } else if (password.equals(password.toLowerCase())) {
            return "Password must contain at least one uppercase letter";
        } else if (password.contains(" ")) {
            return "Password must not contain spaces";
        } else if (!password.equals(confirmPassword)) {
            return "Passwords do not match";
        } else {
            return ""; // No error message
        }
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
            String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            String mimeType = getContentResolver().getType(photoUri);
            this.base64DataUri = "data:" + mimeType + ";base64," + base64Image;

            // Display the photo in an ImageView
            ImageView photoPreview = binding.photoPreview; // Replace with your ImageView's ID
            photoPreview.setImageURI(photoUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class UsernameFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            StringBuilder filteredStringBuilder = new StringBuilder();

            // Iterate through each character in the input
            for (int i = start; i < end; i++) {
                char currentChar = source.charAt(i);

                // Check if the character is a lowercase letter (a-z), ".", or "_"
                if (Character.isLetter(currentChar) || currentChar == '.' || currentChar == '_') {
                    filteredStringBuilder.append((currentChar + "").toLowerCase());
                }
            }

            // Return the filtered input
            return filteredStringBuilder.toString();
        }
    }


}