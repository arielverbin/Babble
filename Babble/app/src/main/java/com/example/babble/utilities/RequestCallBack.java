package com.example.babble.utilities;

// used in requests for server. Defines that to do after success or failure.
public interface RequestCallBack {
    default void onFailure(String error){}
    default void onSuccess(){}
}
