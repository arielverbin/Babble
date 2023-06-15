package com.example.babble.chats;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.babble.R;
import com.example.babble.databinding.ActivityChatBinding;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("Ross Geller");

        // Inflate the custom action bar layout
        View actionBarLayout = getLayoutInflater().inflate(R.layout.chat_actionbar_layout, null);

        // Set the custom view for the action bar
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(actionBarLayout);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        LinearLayout layoutMessageList = findViewById(R.id.layoutMessageList);

        List<Message> messageList = getMessages(); // Replace with your own method to retrieve the message list

        // Initializing the chat going through all of messages and present each message in the right form (whether its a sender's or receiver's
        MessageListAdapter messageListAdapter = new MessageListAdapter(this, messageList);
        List<View> messageViews = messageListAdapter.getMessageViews();

        for (View messageView : messageViews) {
            layoutMessageList.addView(messageView);
        }

        // Auto scroll all the way down.
        ScrollView scrollView = findViewById(R.id.scrollViewMessages);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
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

    private List<Message> getMessages() {
        List<Message> messageList = new ArrayList<>();

        // Create some sample messages
        Message message1 = new Message("Hello, how are you?", true);
        Message message2 = new Message("Hi John, I'm doing great. How about you?", false);
        Message message3 = new Message("I'm doing well too. Thanks for asking!", true);

        // Add the messages to the list
        messageList.add(message1);
        messageList.add(message2);
        messageList.add(message3);
        messageList.add(message1);
        messageList.add(message2);
        messageList.add(message3);

        return messageList;
    }

}