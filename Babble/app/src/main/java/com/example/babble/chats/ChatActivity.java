package com.example.babble.chats;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inflate the custom action bar layout
        View actionBarLayout = getLayoutInflater().inflate(R.layout.chat_actionbar_layout, null);

        // Set the custom view for the action bar
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(actionBarLayout);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "AppDB")
                .allowMainThreadQueries().build();
        messageDao = db.messageDao();

        handleContact();

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

    private void handleContact() {
        messageList = getMessages();
        if(messageList.size() == 0) {
            binding.topOfChatMsg.setText(R.string.noMessages);
            return;
        } else {
            binding.topOfChatMsg.setText(R.string.no_previous_messages);
        }
        LinearLayout layoutMessageList = findViewById(R.id.layoutMessageList);

        // Initializing the chat going through all of messages and present each message in the right form (whether its a sender's or receiver's
        MessageListAdapter messageListAdapter = new MessageListAdapter(this, messageList);
        List<View> messageViews = messageListAdapter.getMessageViews();

        for (View messageView : messageViews) {
            layoutMessageList.addView(messageView);
        }
    }
    private List<Message> getMessages() {
        Intent intent = getIntent();
        String chatIdString = intent.getStringExtra("chatId");

        if (chatIdString != null) {
            int chatId = Integer.parseInt(chatIdString);
            return messageDao.getChat(chatId);
        } else {
            // Error - return an empty list.
            return new ArrayList<>();
        }
    }



}