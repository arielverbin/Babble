package com.example.babble.utilities;

import com.example.babble.entities.Message;

public interface FirebaseMessagingCallback {
    void updateUI(Message message, String senderUsername);
}
