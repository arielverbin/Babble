package com.example.babble;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.babble.databinding.ActivityAddContactBinding;

public class AddContactActivity extends AppCompatActivity {

    ActivityAddContactBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageButton cancelBtn = binding.cancelAdd;
        cancelBtn.setOnClickListener(view -> finish());
    }
}