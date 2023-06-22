package com.example.babble.chats;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.babble.DateGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity
public class Message {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private final int chatId;
    private String content;
    private String timeSent;

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    public void setTimeSentExtended(String timeSentExtended) {
        this.timeSentExtended = timeSentExtended;
    }

    private String timeSentExtended;
    private final boolean sent;

    private boolean isWatermark;

    public Message(String content, int chatId ,boolean sent) {
        this.chatId = chatId;
        this.content = content;
        this.timeSent = getTimestamp();
        this.sent = sent;
        this.isWatermark = false;

        this.timeSent = DateGenerator.getCurrentHour();
        this.timeSentExtended = DateGenerator.getCurrentTimeDay();
    }

    public String getTimeSent() {
        return timeSent;
    }

    public String getTimeSentExtended() {
        return timeSentExtended;
    }

    public void setWatermark(boolean watermark) {
        isWatermark = watermark;
    }

    public Message convertToWatermark() {
        this.isWatermark = true;
        return this;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSent() {
        return sent;
    }

    public boolean isWatermark() {
        return isWatermark;
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