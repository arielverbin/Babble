package com.example.babble.contacts;

import android.graphics.Bitmap;
import android.media.Image;

public class Contact {
    private String username;
    private String displayName;
    private Bitmap profilePicture;
    private String lastMessage;
    private String timeChatted;

    public Contact(String username, String displayName, Bitmap profilePicture, String lastMessage, String timeChatted) {
        this.username = username;
        this.displayName = displayName;
        this.profilePicture = profilePicture;
        this.lastMessage = lastMessage;
        this.timeChatted = timeChatted;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTimeChatted() {
        return timeChatted;
    }

    public void setTimeChatted(String timeChatted) {
        this.timeChatted = timeChatted;
    }
}
