package com.example.babble.registeration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.babble.api.UserAPI;
import com.example.babble.contacts.ContactsActivity;
import com.example.babble.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

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

        Button loginBtn = binding.btnRegister;

        loginBtn.setOnClickListener(v -> {
            TextView errors = binding.errors;

            //need to check that this username does not already exist
            EditText username = binding.usernameInput;
            EditText displayName = binding.nameInput;

            //need to implement the photo too
            //Button pic = binding.selectPhotoButton;

            EditText password = binding.passwordInput;
            String passwordText = password.getText().toString();

            boolean containsDigit = passwordText.matches(".*\\d.*");
            boolean containsUppercase = !passwordText.equals(passwordText.toLowerCase());
            boolean containsSpaces = passwordText.contains(" ");


            EditText confirmPassword = binding.confirmPasswordInput;
            String confirmPasswordText = confirmPassword.getText().toString();

            if (passwordText.length() < 8) {
                errors.setText("Password must be at least 8 characters long");
            } else if (!containsDigit) {
                errors.setText("Password must contain at least one digit");
            } else if (!containsUppercase) {
                errors.setText("Password must contain at least one uppercase letter");
            } else if (containsSpaces) {
                errors.setText("Password must not contain spaces");
            } else if (!passwordText.equals(confirmPasswordText)) {
                errors.setText("Passwords do not match");
            } else{
                User user = new User(username.getText().toString(), displayName.getText().toString(),
                                    passwordText, "");
                UserAPI userAPI = new UserAPI();
                userAPI.post(user);
                errors.setText("");
//                Intent i = new Intent(RegisterActivity.this, ContactsActivity.class);
//                startActivity(i);
            }

        });
    }

}