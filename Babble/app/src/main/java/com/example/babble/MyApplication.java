package com.example.babble;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application{
    public static Context context;
    public static String token;
    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
    }
}
