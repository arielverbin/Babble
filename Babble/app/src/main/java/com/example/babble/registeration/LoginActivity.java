package com.example.babble.registeration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.babble.contacts.ContactsActivity;
import com.example.babble.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

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

        Button loginBtn = binding.btnLogin;
        loginBtn.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, ContactsActivity.class);
            startActivity(i);
        });
    }
}