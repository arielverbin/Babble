package com.example.babble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.babble.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ImageButton cancel = binding.cancel;
        cancel.setOnClickListener(view -> finish());

        TextView logOutBtn = binding.logOutBtn;
        logOutBtn.setOnClickListener(view -> {
            Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
            startActivity(i);
        });
    }
}