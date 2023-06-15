package com.example.babble.chats;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {
    private final String content;
    private final boolean sent;

    public Message(String content, boolean sent) {
        this.content = content;
        this.sent = sent;
    }

    public String getContent() {
        return content;
    }

    public boolean isSent() {
        return sent;
    }

    public String getTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        return dateFormat.format(new Date());
    }

}