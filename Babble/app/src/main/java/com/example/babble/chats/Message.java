package com.example.babble.chats;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity
public class Message {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private final int chatId;
    private final String content;
    private final boolean sent;

    public Message(String content, int chatId ,boolean sent) {
        this.chatId = chatId;
        this.content = content;
        this.sent = sent;
    }

    public String getContent() {
        return content;
    }

    public boolean isSent() {
        return sent;
    }

    public int getChatId(){
        return chatId;
    }
    public int getId(){
        return id;
    }


    public void setId(int id){
        this.id = id;
    }

    public String getTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        return dateFormat.format(new Date());
    }

}