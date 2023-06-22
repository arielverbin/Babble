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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babble.R;
import com.example.babble.contacts.ContactsViewModel;
import com.example.babble.databinding.ActivityChatBinding;


public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    private ChatsViewModel chatsViewModel;
    private ContactsViewModel contactsViewModel;

    private MessageListAdapter messageListAdapter;

    private int currentChatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Receive current chat id, given by the ContactsActivity.
        Intent intent = getIntent();
        String chatIdString = intent.getStringExtra("chatId");
        if (chatIdString != null) {
            currentChatId = Integer.parseInt(chatIdString);
        } else finish();

        // initialize the view models.
        contactsViewModel = new ViewModelProvider(this).get(ContactsViewModel.class);
        chatsViewModel = new ViewModelProvider(this).get(ChatsViewModel.class);
        chatsViewModel.setChat(currentChatId);

        handleContact();

        handleActionBar();

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
            contactName.setText(contactsViewModel.getContactById(currentChatId).getDisplayName());
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
        RecyclerView recyclerView = findViewById(R.id.recyclerViewMessages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Set adapter to message list.
        messageListAdapter = new MessageListAdapter(this);
        recyclerView.setAdapter(messageListAdapter);

        // observe for initial creation - fetch all messages.
        chatsViewModel.getAllMessages().observe(this, messageList -> {
            if (messageList.size() == 0) {
                messageList.add(0, new Message("Looks like there aren't any messages yet.", currentChatId, true).convertToWatermark());
            } else {
                messageList.add(0, new Message("No previous messages.", currentChatId, true).convertToWatermark());
            }

            chatsViewModel.getAllMessages().observe(this, messages -> {
                messageListAdapter.setMessages(messages);
                scrollDown(); // scroll to the bottom.
            });
        });
    }


    private void handleSendMessage() {
        ImageButton sendBtn = binding.sendBtn;

        sendBtn.setOnClickListener(view -> {
            EditText messageInput = binding.messageBox;
            String messageContent = messageInput.getText().toString();

            if (messageContent.equals("")) return; // prevent empty messages.
            messageInput.setText(""); // empty text box.

            Message message = new Message(messageContent, currentChatId, true);

            // insert new message and observe for updating the adapter.
            chatsViewModel.insertMessage(message);
            chatsViewModel.getAllMessages().observe(this, messages -> {
                        Message newMessage = messages.get(messages.size() - 1);
                        messageListAdapter.insertLastMessage(newMessage);

                        contactsViewModel.setLastMessage(currentChatId, newMessage.getContent(), newMessage.getTimeSentExtended());
                    }
            );

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


    // delete a chat.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.deleteChat) {
            contactsViewModel.deleteContactById(currentChatId);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}