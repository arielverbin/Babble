package com.example.babble.chats;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ChatsViewModel extends AndroidViewModel {

    private ChatsRepository chatsRepository;

    private final Application application;

    private LiveData<List<Message>> allMessages;

    public ChatsViewModel(@NonNull Application application) {
        super(application);

        this.application = application;
    }

    public void setChat(int chatId) {
        chatsRepository = new ChatsRepository(application, chatId);
        allMessages = chatsRepository.getAllMessages();
    }

    public LiveData<List<Message>> getAllMessages() {
        return allMessages;
    }

    public Message getMessageById(int id) {
        return chatsRepository.getMessage(id);
    }


    public void insertMessage(Message message) {
        chatsRepository.insertMessage(message);
    }

    public void updateMessage(Message message) {
        chatsRepository.updateMessage(message);
    }

    public void deleteMessage(Message message) {
        chatsRepository.deleteMessage(message);
    }
}
