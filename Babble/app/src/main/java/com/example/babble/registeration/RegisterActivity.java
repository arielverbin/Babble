package com.example.babble.registeration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.example.babble.R;
import com.example.babble.api.UserAPI;
import com.example.babble.contacts.ContactsActivity;
import com.example.babble.databinding.ActivityRegisterBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
        if(getSupportActionBar() != null)
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

            //need to check that this username does not already exist
            EditText username = binding.usernameInput;
            EditText displayName = binding.nameInput;
            ImageView photoPreview = binding.photoPreview;// Replace with your ImageView's ID

            //need to implement the photo too
            //Button pic = binding.selectPhotoButton;

            EditText password = binding.passwordInput;
            String passwordText = password.getText().toString();

            boolean containsDigit = passwordText.matches(".*\\d.*");
            boolean containsUppercase = !passwordText.equals(passwordText.toLowerCase());
            boolean containsSpaces = passwordText.contains(" ");


            EditText confirmPassword = binding.confirmPasswordInput;
            String confirmPasswordText = confirmPassword.getText().toString();
//            if (username.getText().toString().equals("") || displayName.getText().toString().equals("")
//                    || passwordText.equals("") || confirmPasswordText.equals("") ||
//                    photoPreview == null || photoPreview.getDrawable() == null) {
//                errors.setText("All fields are required");
//            }
//            else if (passwordText.length() < 8) {
                         if (passwordText.length() < 8) {

                errors.setText("Password must be at least 8 characters long");
//            } else if (!containsDigit) {
//                errors.setText("Password must contain at least one digit");
//            } else if (!containsUppercase) {
//                errors.setText("Password must contain at least one uppercase letter");
//            } else if (containsSpaces) {
//                errors.setText("Password must not contain spaces");
//            } else if (!passwordText.equals(confirmPasswordText)) {
//                errors.setText("Passwords do not match");
            } else{
                errors.setText("");
                User user = new User(username.getText().toString(), displayName.getText().toString(),
                                    passwordText, this.base64Image);
                UserAPI userAPI = new UserAPI();

                userAPI.post(user, RegisterActivity.this);

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

//    private void saveAndDisplayImage(Uri photoUri) {
//        // Save the photoUri to a location or perform any necessary operations
//
//        // Display the photo in an ImageView
//        ImageView photoPreview = binding.photoPreview;// Replace with your ImageView's ID
//        photoPreview.setImageURI(photoUri);
//    }


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