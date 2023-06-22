package com.example.babble.contacts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.babble.R;
import com.example.babble.databinding.ActivityAddContactBinding;

public class AddContactActivity extends AppCompatActivity {

    private ContactsViewModel contactsViewModel;
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

        // initialize view model.
        contactsViewModel = new ViewModelProvider(this).get(ContactsViewModel.class);

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
            if (username.isEmpty()) return;

            // Error if the username already exists.
            Contact existingContact = contactsViewModel.getContactByUsername(username);
            if(existingContact != null) {
                TextView errorMsg = binding.errorMsg;
                errorMsg.setText(R.string.username_already_exists);
                CardView errorCard = binding.errorCard;
                errorCard.setVisibility(View.VISIBLE);
                return;
            }

            Contact newContact = new Contact(username, username, null, "This conversation is new", "");

            // insert new contact to database.
            contactsViewModel.insertContact(newContact);

            finish();
        });
    }
}
