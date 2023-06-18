package com.example.babble.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

import com.example.babble.AppDB;
import com.example.babble.databinding.ActivityAddContactBinding;

public class AddContactActivity extends AppCompatActivity {

    private AppDB db;
    private ContactDao contactDao;

    private ActivityAddContactBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set actionbar title.
        setTitle("Add Contact");
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "AppDB")
                .allowMainThreadQueries().build();

        contactDao = db.contactDao();
        handleSave();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void handleSave() {
        binding.addBtn.setOnClickListener(view -> {
            String username = binding.newContactInput.getText().toString();
            Contact contact = new Contact(username, username, null, "This conversation is new.", "");

            contactDao.insert(contact);

            finish();
        });
    }
}
