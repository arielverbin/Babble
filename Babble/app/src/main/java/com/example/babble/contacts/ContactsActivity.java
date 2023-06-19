package com.example.babble.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.babble.AppDB;
import com.example.babble.R;
import com.example.babble.SettingsActivity;
import com.example.babble.chats.ChatActivity;
import com.example.babble.databinding.ActivityContactsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private ActivityContactsBinding binding;
    private ListView contactsListView;
    private ContactsAdapter contactsAdapter;

    private AppDB db;
    private ContactDao contactDao;
    private List<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        contactsListView = binding.contactList;

        // Set actionbar title.
        setTitle("Chats");

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "AppDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        contactDao = db.contactDao();
        contactList = contactDao.index();

        contactsAdapter = new ContactsAdapter(contactList);
        contactsListView.setAdapter(contactsAdapter);
        contactsListView.setOnItemClickListener((parent, view, position, id) -> {
            Contact selectedContact = contactList.get(position);
            Intent intent = new Intent(ContactsActivity.this, ChatActivity.class);
            intent.putExtra("chatId", "" + selectedContact.getId());
            startActivity(intent);
        });

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
        refreshContactList();
    }

    private void refreshContactList() {
        contactList.clear();
        contactList.addAll(contactDao.index());
        contactsAdapter.notifyDataSetChanged();
    }
}
