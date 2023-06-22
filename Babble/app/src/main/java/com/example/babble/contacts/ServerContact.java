package com.example.babble.contacts;

import com.example.babble.DateGenerator;
import com.example.babble.registeration.User;


// Contact in server format.
public class ServerContact {
    private String id;
    private User user;
    private LastMessage lastMessage;

    public ServerContact(String id, User user, LastMessage lastMessage) {
        this.id = id;
        this.user = user;
        this.lastMessage = lastMessage;
    }


    // Convert server contact to ROOM contact.
    public Contact convertToContact() {
        if(lastMessage == null) {
            return new Contact(id, user.getUsername(), user.getDisplayName(), user.getProfilePic(),
                    "This conversation is new.", "");
        }

        return new Contact(id, user.getUsername(), user.getDisplayName(), user.getProfilePic(),
                lastMessage.getContent(), DateGenerator.toTimeDay(lastMessage.getCreated()));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public void setUsername(String username) {
        this.user.setUsername(username);
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDisplayName() {
        return user.getDisplayName();
    }

    public void setDisplayName(String displayName) {
        user.setDisplayName(displayName);
    }

    public String getProfilePicturePath() {
        return user.getProfilePic();
    }

    public void setProfilePicturePath(String profilePicturePath) {
        user.setProfilePic(profilePicturePath);
    }

    public LastMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(LastMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

}

