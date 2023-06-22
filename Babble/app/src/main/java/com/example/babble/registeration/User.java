package com.example.babble.registeration;

import java.io.Serializable;

public class User implements Serializable{
    private String username;
    private String displayName;
    private String password;
    private String profilePic;

    public User(String username, String displayName, String password, String profilePic) {
        this.username = username;
        this.displayName = displayName;
        this.password = password;
        this.profilePic = profilePic;

    }

    public User(String username, String displayName, String profilePic) {
        this.username = username;
        this.displayName = displayName;
        this.profilePic = profilePic;

        // known - used for contact.
        this.password = null;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;

        // unknown - used for login.
        this.profilePic = null;
    }

    public User(String username) {
        this.username = username;

        // unknown - used for message.
        this.displayName = null;
        this.password = null;
        this.profilePic = null;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPassword() {
        return password;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
