package com.example.babble.serverObjects;

public class UserDataToSettings {
    private String newPic;
    private String newDisplayName;

    public UserDataToSettings(String newPic, String newDisplayName) {
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
