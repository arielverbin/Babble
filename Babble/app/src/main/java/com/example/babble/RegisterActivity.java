package com.example.babble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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
            Intent i = new Intent(RegisterActivity.this, ContactsActivity.class);
            startActivity(i);
        });
    }

}