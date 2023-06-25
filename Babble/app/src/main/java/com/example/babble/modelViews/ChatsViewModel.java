package com.example.babble.modelViews;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.babble.entities.Message;
import com.example.babble.utilities.RequestCallBack;
import com.example.babble.repositories.ChatsRepository;

import java.util.List;

public class ChatsViewModel extends AndroidViewModel {

    private ChatsRepository chatsRepository;

    private final Application application;

    private LiveData<List<Message>> allMessages;

    public ChatsViewModel(@NonNull Application application) {
        super(application);

        this.application = application;
    }

    public void setChat(String chatId, String username) {
        chatsRepository = new ChatsRepository(application, chatId, username);
        allMessages = chatsRepository.getAllMessages();
    }

    public LiveData<List<Message>> getAllMessages() {
        return allMessages;
    }

    public Message getMessageById(String id) {
        return chatsRepository.getMessage(id);
    }


    public void insertMessage(Message message, RequestCallBack callBack) {
        chatsRepository.insertMessage(message, callBack);
    }

}
