package com.example.babble.contacts;

import com.example.babble.registeration.User;

public class ContactAfterAdd {
    private int id;
    private UserAfterAdd user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserAfterAdd getUser() {
        return user;
    }

    public void setUser(UserAfterAdd user) {
        this.user = user;
    }
}
