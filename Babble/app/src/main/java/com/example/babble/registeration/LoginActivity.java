package com.example.babble.registeration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babble.api.UserAPI;
import com.example.babble.contacts.ContactsActivity;
import com.example.babble.databinding.ActivityLoginBinding;
import com.example.babble.databinding.ActivityRegisterBinding;

public class LoginActivity extends AppCompatActivity implements PostCallback{

    private ActivityLoginBinding binding;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        TextView errors = binding.errors;

        // Disable the action bar.
        if(getSupportActionBar() != null)
            getSupportActionBar().hide();

        loginButton = binding.btnLogin;
        loginButton.setOnClickListener(v -> {
            // Handle login button click event
            // Add your login logic here
        });

        TextView registerLink = binding.registerLink;

        registerLink.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        });
        errors.setText("");
        Button loginBtn = binding.btnLogin;
        loginBtn.setOnClickListener(v -> {
            EditText usernameInput = binding.usernameInput;
            EditText passwordInput = binding.passwordInput;

            LoginUser loginUser = new LoginUser(usernameInput.getText().toString(),
                                                passwordInput.getText().toString());

            UserAPI userAPI = new UserAPI();

            userAPI.login(loginUser, LoginActivity.this, this);

        });
    }
    @Override
    public void onPostFail() {
//        binding = ActivityLoginBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        TextView errors = binding.errors;

        errors.setText("Username or password does not match");

    }
}