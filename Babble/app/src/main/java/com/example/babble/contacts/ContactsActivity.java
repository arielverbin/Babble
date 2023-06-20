package com.example.babble.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.example.babble.AppDB;
import com.example.babble.R;
import com.example.babble.SettingsActivity;
import com.example.babble.chats.ChatActivity;
import com.example.babble.databinding.ActivityContactsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private ActivityContactsBinding binding;
    private ListView contactsListView;
    private ContactsAdapter contactsAdapter;

    private ContactsViewModel contactsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set actionbar title.
        setTitle("Chats");

        // initialize view model
        contactsViewModel = new ViewModelProvider(this).get(ContactsViewModel.class);

        // create a new adapter for the contact list.
        contactsAdapter = new ContactsAdapter();
        contactsListView = binding.contactList;
        contactsListView.setAdapter(contactsAdapter);

        // observe for initial creation - fetch all contacts.
        contactsViewModel.getAllContacts().observe(this, contacts -> {
            contactsAdapter.setContacts(contacts);
        });

        // clicking a contact starts the ChatActivity, given the current charId.
        contactsListView.setOnItemClickListener((parent, view, position, id) -> {
            Contact selectedContact = (Contact) contactsAdapter.getItem(position);
            Intent intent = new Intent(ContactsActivity.this, ChatActivity.class);
            intent.putExtra("chatId", "" + selectedContact.getId());
            startActivity(intent);
        });

        // clicking the floating action button start the AddContactActivity.
        FloatingActionButton addContactButton = binding.addContactBtn;
        addContactButton.setOnClickListener(view -> {
            Intent intent = new Intent(ContactsActivity.this, AddContactActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chats_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_settings) {
            Intent intent = new Intent(ContactsActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // observe for resuming the activity (after exit from ChatActivity).
        contactsViewModel.getAllContacts().observe(this, contacts -> contactsAdapter.setContacts(contacts));
    }

}
