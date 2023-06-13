package com.example.babble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.babble.databinding.ActivityContactsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    List<Contact> contactList;
    private ActivityContactsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ListView contactsView = binding.contactList;

        contactList = generateContacts();
        final ContactsAdapter feedAdapter = new ContactsAdapter(contactList);
        contactsView.setAdapter(feedAdapter);
        contactsView.setOnItemClickListener((parent, view, position, id) -> {
            // Contact selectedContact = contactList.get(position);
            Intent i = new Intent(ContactsActivity.this, ChatActivity.class);
            startActivity(i);
        });

        FloatingActionButton addContact = binding.addContactBtn;
        addContact.setOnClickListener(view -> {
            Intent i = new Intent(ContactsActivity.this, AddContactActivity.class);
            startActivity(i);
        });

        ImageButton settings = binding.settings;
        settings.setOnClickListener(view -> {
            Intent i = new Intent(ContactsActivity.this, SettingsActivity.class);
            startActivity(i);
        });
    }

    private List<Contact> generateContacts() {
        List<Contact> contacts = new ArrayList<>();
        Bitmap examplePic = BitmapFactory.decodeResource(getResources(), R.drawable.walter);

        contacts.add(new Contact("ross", "Ross Geller", examplePic, "We were on a break!", "Yesterday"));
        contacts.add(new Contact("ross", "Ross Geller", examplePic, "We were on a break!", "Yesterday"));
        contacts.add(new Contact("ross", "Ross Geller", examplePic, "We were on a break!", "Yesterday"));
        contacts.add(new Contact("ross", "Ross Geller", examplePic, "We were on a break!", "Yesterday"));
        contacts.add(new Contact("ross", "Ross Geller", examplePic, "We were on a break!", "Yesterday"));
        contacts.add(new Contact("ross", "Ross Geller", examplePic, "We were on a break!", "Yesterday"));
        contacts.add(new Contact("ross", "Ross Geller", examplePic, "We were on a break!", "Yesterday"));
        contacts.add(new Contact("ross", "Ross Geller", examplePic, "We were on a break!", "Yesterday"));
        contacts.add(new Contact("ross", "Ross Geller", examplePic, "We were on a break!", "Yesterday"));
        contacts.add(new Contact("ross", "Ross Geller", examplePic, "We were on a break!", "Yesterday"));

        return contacts;
    }
}