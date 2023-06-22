package com.example.babble.chats;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.example.babble.R;
import java.util.ArrayList;
import java.util.List;

public class MessageListAdapter {
    private final Context context;
    private final List<Message> messageList;

    public MessageListAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    public List<View> getMessageViews() {
        List<View> views = new ArrayList<>();

        for (Message message : messageList) {
            View messageView = inflateMessageView(message);
            TextView messageTextView = messageView.findViewById(R.id.messageTextView);
            TextView timestampTextView = messageView.findViewById(getTimestampTextViewId(message));

            messageTextView.setText(message.getContent());
            timestampTextView.setText(message.getTimestamp());
            // Customize the messageView with the message details if needed

            String messageText = messageTextView.getText().toString(); // Retrieve the text from messageTextView
            Log.d("Message", messageText); // Print the message

            views.add(messageView);
        }

        return views;
    }

    private View inflateMessageView(Message message) {
        int layoutResId = message.isSent() ? R.layout.sent_message_layout : R.layout.recieved_message_layout;
        return LayoutInflater.from(context).inflate(layoutResId, null);
    }

    private int getTimestampTextViewId(Message message) {
        return message.isSent() ? R.id.text_gchat_timestamp_me : R.id.text_gchat_timestamp_other;
    }
}
