package com.example.babble.chats;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babble.DateGenerator;
import com.example.babble.MyApplication;
import com.example.babble.R;
import com.example.babble.contacts.Contact;
import com.example.babble.contacts.ContactsViewModel;
import com.example.babble.databinding.ActivityChatBinding;
import com.example.babble.registeration.RequestCallBack;

import java.util.LinkedList;
import java.util.List;


public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    private ChatsViewModel chatsViewModel;
    private ContactsViewModel contactsViewModel;

    private MessageListAdapter messageListAdapter;

    private String currentChatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Receive current chat id, given by the ContactsActivity.
        Intent intent = getIntent();
        String chatIdString = intent.getStringExtra("chatId");
        if (chatIdString != null) {
            currentChatId = chatIdString;
        } else finish();

        // initialize the view models.
        contactsViewModel = new ViewModelProvider(this).get(ContactsViewModel.class);
        chatsViewModel = new ViewModelProvider(this).get(ChatsViewModel.class);
        // set current chat id and current username to view model.
        chatsViewModel.setChat(currentChatId, MyApplication.getUsername());

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

            // set contact name.
            TextView contactName = actionBarLayout.findViewById(R.id.contactName);
            contactName.setText(contactsViewModel.getContactById(currentChatId).getDisplayName());

            // set contact picture
            Contact currentContact = contactsViewModel.getContactById(currentChatId);
            String base64ProfilePic = currentContact.getProfilePicture();
            // remove "data:image/jpg;base64"
            String pureBase64Encoded = base64ProfilePic.substring(base64ProfilePic.indexOf(",") + 1);
            // decode to bitmap.
            byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            ImageView profilePic = actionBarLayout.findViewById(R.id.profileChatImage);
            profilePic.setImageBitmap(decodedByte);
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
            messageListAdapter.setMessages(addWatermarks(messageList));
            scrollDown(); // scroll to the bottom.
        });
    }


    private void handleSendMessage() {
        ImageButton sendBtn = binding.sendBtn;

        sendBtn.setOnClickListener(view -> {
            EditText messageInput = binding.messageBox;
            String messageContent = messageInput.getText().toString().trim();

            if (messageContent.equals("")) return; // prevent empty messages.
            messageInput.setText(""); // empty text box.

            Message message = new Message(messageContent, currentChatId,
                    DateGenerator.getCurrentHour(), DateGenerator.getCurrentTimeDay(), true);

            // insert new message and observe for updating the adapter.
            chatsViewModel.insertMessage(message, new RequestCallBack() {
                @Override
                public void onFailure(String error) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                    builder.setTitle("Error Sending Message");
                    builder.setMessage(error);
                    builder.setPositiveButton(ChatActivity.this.getString(R.string.ok), (dialog, which) -> dialog.dismiss());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                @Override
                public void onSuccess() {
                    chatsViewModel.getAllMessages().observe(ChatActivity.this, messages -> {
                        if(messages.size() > 0) {
                            Message newMessage = messages.get(messages.size() - 1);

                            contactsViewModel.setLastMessage(currentChatId,
                                    newMessage.getMsg(),
                                    newMessage.getTimeSentExtended(),
                                    // no need for handling error/success -
                                    // not the end of the world if it fails.
                                    new RequestCallBack() {
                                    });
                        }
                    });
                }
            });
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
            contactsViewModel.deleteContact(currentChatId, new RequestCallBack() {
                @Override
                public void onFailure(String error) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                    builder.setTitle("Error Deleting Contact");
                    builder.setMessage(error);
                    builder.setPositiveButton(ChatActivity.this.getString(R.string.ok), (dialog, which) -> dialog.dismiss());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                @Override
                public void onSuccess() {
                    finish();
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public List<Message> addWatermarks(List<Message> messageList) {
        List<Message> modifiedList = new LinkedList<>();
        if (messageList.size() == 0) {
            modifiedList.add(
                    new Message("Looks like there aren't any messages yet.",
                            currentChatId, "", "",
                            true).convertToWatermark());

            return modifiedList;
        }
        // add "no previous messages" watermark.
        modifiedList.add( new Message("No previous messages.",
                currentChatId, "", "",
                true).convertToWatermark());

        modifiedList.add(new Message(messageList.get(0).getDaySent(),
                currentChatId, "", "", true).convertToWatermark());

        for (int i = 0; i < messageList.size() - 1; i++) {
            Message currentMessage = messageList.get(i);
            Message nextMessage = messageList.get(i + 1);
            if(nextMessage == null) break;

            modifiedList.add(currentMessage); // Add current message

            if (!currentMessage.getDaySent().equals(nextMessage.getDaySent())) {
                modifiedList.add(new Message(nextMessage.getDaySent(),
                        currentChatId, "", "", true).convertToWatermark()); // Add watermark for the next day
            }
        }

        modifiedList.add(messageList.get(messageList.size() - 1)); // Add the last message

        return modifiedList;
    }

}