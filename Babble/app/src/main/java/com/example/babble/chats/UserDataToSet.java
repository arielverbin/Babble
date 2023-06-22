package com.example.babble.chats;

public class UserDataToSet {
    private String newPic;
    private String newDisplayName;

    public UserDataToSet(String newPic, String newDisplayName) {
        this.newPic = newPic;
        this.newDisplayName = newDisplayName;
    }

    public String getNewPic() {
        return newPic;
    }

    public void setNewPic(String newPic) {
        this.newPic = newPic;
    }

    public String getNewDisplayName() {
        return newDisplayName;
    }

    public void setNewDisplayName(String newDisplayName) {
        this.newDisplayName = newDisplayName;
    }
}
