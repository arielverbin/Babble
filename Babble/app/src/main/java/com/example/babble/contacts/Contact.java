package com.example.babble.contacts;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contact {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String displayName;
    private String profilePicturePath;
    private String lastMessage;
    private String timeChatted;

    public Contact(String username, String displayName, String profilePicturePath, String lastMessage, String timeChatted) {
        this.username = username;
        this.displayName = displayName;
        this.profilePicturePath = profilePicturePath;
        this.lastMessage = lastMessage;
        this.timeChatted = timeChatted;
    }

    public Contact(ContactFromServer serverContact) {
        this.username = serverContact.getUser().getUsername();
        this.displayName = serverContact.getUser().getDisplayName();
        this.profilePicturePath = serverContact.getUser().getProfilePic();
        this.lastMessage = serverContact.getLastMessage().getContent();
        this.timeChatted = serverContact.getLastMessage().getCreated();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
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
