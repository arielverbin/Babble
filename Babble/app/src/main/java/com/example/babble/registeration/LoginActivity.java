package com.example.babble.registeration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.babble.API.UserAPI;
import com.example.babble.R;
import com.example.babble.contacts.ContactsActivity;
import com.example.babble.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        // Disable the action bar.
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        Button loginButton = binding.btnLogin;
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
            EditText usernameInput = binding.usernameInput;
            EditText passwordInput = binding.passwordInput;

            if(usernameInput.getText().toString().equals("") ||
                    passwordInput.getText().toString().equals("")) {
                TextView errors = binding.errors;
                CardView errorCard = binding.errorCard;
                errorCard.setVisibility(View.VISIBLE);
                errors.setText(R.string.all_fields_are_required);
                return;
            }

            User loginUser = new User(usernameInput.getText().toString(),
                    passwordInput.getText().toString());

            UserAPI userAPI = new UserAPI();

            userAPI.login(loginUser, LoginActivity.this, new RequestCallBack() {
                // display error message.
                @Override
                public void onFailure(String error) {
                    TextView errors = binding.errors;
                    CardView errorCard = binding.errorCard;
                    errorCard.setVisibility(View.VISIBLE);
                    errors.setText(error);
                }
                // success! start contacts activity.
                @Override
                public void onSuccess() {
                    Intent intent = new Intent(LoginActivity.this, ContactsActivity.class);
                    startActivity(intent);
                }
            });

        });
    }
}