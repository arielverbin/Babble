package com.example.babble.registeration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.example.babble.API.UserAPI;
import com.example.babble.contacts.ContactsActivity;
import com.example.babble.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    private String base64Image;
    private ActivityRegisterBinding binding;
    private static final int PICK_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        // Disable the actionbar.
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        TextView loginLink = binding.loginLink;

        loginLink.setOnClickListener(v -> {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        });

        Button selectPhotoButton = binding.selectPhotoButton;
        selectPhotoButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });

        Button loginBtn = binding.btnRegister;

        loginBtn.setOnClickListener(v -> {
            TextView errors = binding.errors;

            String username = binding.usernameInput.getText().toString();
            String displayName = binding.nameInput.getText().toString();
            String password = binding.passwordInput.getText().toString();
            String confirmPassword = binding.confirmPasswordInput.getText().toString();
            Drawable photoPreview = binding.photoPreview.getDrawable();

            String errorMsg = produceErrorMsg(displayName, username,
                    password, confirmPassword,photoPreview);

            if (!errorMsg.isEmpty()) {
                CardView errorCard = binding.errorCard;
                errorCard.setVisibility(View.VISIBLE);
                errors.setText(errorMsg);
            } else {
                User user = new User(username, displayName,
                        password, this.base64Image);
                UserAPI userAPI = new UserAPI();
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
                    }
                });
            }
        });
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
            this.base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            // Perform any necessary operations with the base64Image
            // (e.g., save it to a location or send it to a server)

            // Display the photo in an ImageView
            ImageView photoPreview = binding.photoPreview; // Replace with your ImageView's ID
            photoPreview.setImageURI(photoUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}