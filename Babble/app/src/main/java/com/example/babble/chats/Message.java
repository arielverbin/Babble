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

    private final String chatId;
    private String msg;
    private String created;


    private String timeSentExtended;
    private final boolean sent;

    private boolean isWatermark;

    public void setCreated(String created) {
        this.created = created;
    }

    public void setTimeSentExtended(String timeSentExtended) {
        this.timeSentExtended = timeSentExtended;
    }

    public Message(String msg, String chatId, String created,
                   String timeSentExtended, boolean sent) {

        this.chatId = chatId;
        this.msg = msg;
        this.sent = sent;
        this.isWatermark = false;

        this.created = created;
        this.timeSentExtended = timeSentExtended;
    }

    public String getCreated() {
        return created;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSent() {
        return sent;
    }

    public boolean isWatermark() {
        return isWatermark;
    }

    public String getChatId(){
        return chatId;
    }
    public int getId(){
        return id;
    }


    public void setId(int id){
        this.id = id;
    }

    public String getDaySent() {
        return DateGenerator.timeDayToDay(this.timeSentExtended);
    }

    public String getTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        return dateFormat.format(new Date());
    }

}