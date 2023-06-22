package com.example.babble;

import android.app.Application;
import android.content.Context;

import com.example.babble.registeration.User;

public class MyApplication extends Application{
    public static Context context;

    private static String token;
    public static User currentUser;
    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
    }

    public static void setUser(User user) {
        currentUser = user;
    }

    public static String getUsername() {
        return currentUser.getUsername();
    }

    public static void setToken(String newToken) {
        token = newToken;
    }

    public static String getToken() {
        return token;
    }


}
