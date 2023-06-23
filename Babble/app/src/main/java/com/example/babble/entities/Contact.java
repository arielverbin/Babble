package com.example.babble.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contact {

    @PrimaryKey
    @NonNull
    private String id;
    private String username;
    private String displayName;
    private String profilePicture;
    private String lastMessage;
    private String timeChatted;

    public Contact(@NonNull String id, String username, String displayName, String profilePicture, String lastMessage, String timeChatted) {
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.profilePicture = profilePicture;
        this.lastMessage = lastMessage;
        this.timeChatted = timeChatted;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
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
