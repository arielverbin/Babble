package com.example.babble.registeration;

public class User {
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
