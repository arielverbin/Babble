package com.example.babble.chats;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.babble.AppDB;
import com.example.babble.R;
import com.example.babble.databinding.ActivityChatBinding;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private AppDB db;
    private MessageDao messageDao;
    private List<Message> messageList;

    private Thread databaseThread;
    private MessageListAdapter messageListAdapter;

    private int currentChatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "AppDB")
                .allowMainThreadQueries().build();
        messageDao = db.messageDao();

        handleContact();

        handleActionBar();

        // Auto scroll all the way down.
        scrollDown();

        handleSendMessage();
    }

    private void handleActionBar() {
        // Inflate the custom action bar layout
        View actionBarLayout = getLayoutInflater().inflate(R.layout.chat_actionbar_layout, null);

        // Set the custom view for the action bar
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(actionBarLayout);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            TextView contactName = actionBarLayout.findViewById(R.id.contactName);
            contactName.setText(db.contactDao().get(currentChatId).getDisplayName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu XML file
        getMenuInflater().inflate(R.menu.chat_actionbar, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void handleContact() {
        messageList = getMessages();
        if (messageList.size() == 0) {
            messageList.add(0, new Message("Looks like there aren't any messages yet.", currentChatId, true).convertToWatermark());
        } else {
            messageList.add(0, new Message("No previous messages.", currentChatId, true).convertToWatermark());
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerViewMessages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        messageListAdapter = new MessageListAdapter(this, messageList);
        recyclerView.setAdapter(messageListAdapter);

        messageListAdapter.notifyItemInserted(messageList.size() - 1);
    }


    private List<Message> getMessages() {
        Intent intent = getIntent();
        String chatIdString = intent.getStringExtra("chatId");

        if (chatIdString != null) {
            currentChatId = Integer.parseInt(chatIdString);
            return messageDao.getChat(currentChatId);
        } else {
            return new ArrayList<>();
        }
    }

    private void handleSendMessage() {
        ImageButton sendBtn = binding.sendBtn;
        sendBtn.setOnClickListener(view -> {
            EditText messageInput = binding.messageBox;
            String messageContent = messageInput.getText().toString();
            if(messageContent.equals("")) return;
            messageInput.setText("");

            Message message = new Message(messageContent, currentChatId, true);
            databaseThread = new Thread(() -> {
                // Insert the chat message in the local database using the DAO
                messageDao.insert(message);
                displayMessage(message);

                // Update last message for the contact.
                db.contactDao().setLastMessage(currentChatId, message.getContent(), message.getTimeSentExtended());

            });
            databaseThread.start();
        });
    }

    private void displayMessage(Message message) {
        runOnUiThread(() -> {
            // Update the chat message list view or adapter
            messageListAdapter.addMessage(message);
            scrollDown();
        });
    }

    private void scrollDown() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewMessages);
        int itemCount = messageListAdapter.getItemCount();
        if (itemCount > 0) {
            recyclerView.scrollToPosition(itemCount - 1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.deleteChat) {
            db.contactDao().deleteById(currentChatId);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}