package com.example.babble.activities;

import static android.webkit.URLUtil.isValidUrl;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.room.Room;

import com.example.babble.API.UserAPI;
import com.example.babble.AppDB;
import com.example.babble.R;
import com.example.babble.databinding.ActivityLoginBinding;
import com.example.babble.entities.Preference;
import com.example.babble.entities.PreferenceDao;
import com.example.babble.utilities.RequestCallBack;
import com.example.babble.serverObjects.User;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private PreferenceDao preferenceDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create a preference dao instance.
        AppDB db = Room.databaseBuilder(LoginActivity.this, AppDB.class, "AppDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        preferenceDao = db.preferenceDao();
        if(preferenceDao.get("token") != null) {
            Intent i = new Intent(LoginActivity.this, ContactsActivity.class);
            startActivity(i);
            finish();
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialize the text in the server url input.
        EditText serverUrlEdit = binding.serverUrlInput;
        if(preferenceDao.get("serverUrl") == null) { //initial entrance to the app!
            showWelcome();
            serverUrlEdit.setText(LoginActivity.this.getString(R.string.BaseUrl));
        } else {
            serverUrlEdit.setText(preferenceDao.get("serverUrl"));
        }

        // Disable the action bar.
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        // 'not registered' button.
        TextView registerLink = binding.registerLink;
        registerLink.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
            finish();
        });

        Button loginBtn = binding.btnLogin;
        loginBtn.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        TextView errors = binding.errors;
        CardView errorCard = binding.errorCard;

        EditText usernameInput = binding.usernameInput;
        EditText passwordInput = binding.passwordInput;
        EditText serverUrlInput = binding.serverUrlInput;

        if(isValidUrl(serverUrlInput.getText().toString())) {
            preferenceDao.set(new Preference("serverUrl", serverUrlInput.getText().toString()));
        } else {
            errorCard.setVisibility(View.VISIBLE);
            errors.setText(R.string.invalid_server_address);
            return;
        }
        preferenceDao.set(new Preference("serverUrl", serverUrlInput.getText().toString()));


        if(usernameInput.getText().toString().equals("") ||
                passwordInput.getText().toString().equals("")) {

            errorCard.setVisibility(View.VISIBLE);
            errors.setText(R.string.all_fields_are_required);
            return;
        }

        User loginUser = new User(usernameInput.getText().toString(),
                passwordInput.getText().toString());

        UserAPI userAPI = new UserAPI(LoginActivity.this);

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
                finish();
            }
        });

    }


    private void showWelcome() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Welcome to Babble :)");
        builder.setMessage("Exchange ideas, express your thoughts, and forge new relationships." +
                "\n\nStart babbling now!");
        builder.setPositiveButton(LoginActivity.this.getString(R.string.ok), (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}