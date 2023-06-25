package com.example.babble.repositories;

import android.content.Context;
import android.content.ContextWrapper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.babble.API.ChatsAPI;
import com.example.babble.AppDB;
import com.example.babble.entities.Message;
import com.example.babble.entities.MessageDao;
import com.example.babble.utilities.RequestCallBack;


import java.util.List;

public class ChatsRepository {

    private final MessageDao messageDao;

    private final Context context;
    private final ChatsAPI api;
    private final String currentChatId;
    private final String currentUsername;
    private final MutableLiveData<List<Message>> messagesListData;

    public ChatsRepository(ContextWrapper context, String chatId, String username) {
        this.currentChatId = chatId;
        this.currentUsername = username;

        AppDB db = Room.databaseBuilder(context, AppDB.class, "AppDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        messageDao = db.messageDao();
        messagesListData = new MessagesListData();

        this.context = context;
        this.api = new ChatsAPI(context);

        updateMessagesList();
    }

    public LiveData<List<Message>> getAllMessages() {
        return messagesListData;
    }

    public Message getMessage(String id) {
        return messageDao.get(id);
    }

    public void insertMessage(Message message, RequestCallBack callback) {
        new Thread(() -> api.sendMessage(this.context, currentChatId, message, new RequestCallBack() {
            // success! update contact list, notifying caller with "success"
            @Override
            public void onSuccess() {
                updateMessagesList();
                callback.onSuccess();
            }
            // notifying caller with "failure"
            @Override
            public void onFailure(String error) {
                callback.onFailure(error);
            }
        })).start();
    }


    private void updateMessagesList() {
        List<Message> messages = messageDao.getChat(currentChatId);
        messagesListData.postValue(messages);
    }


    class MessagesListData extends MutableLiveData<List<Message>> {

        public MessagesListData() {
            super();
            setValue(messageDao.getChat(currentChatId));
        }

        @Override
        public void onActive() {

            new Thread(() -> api.getMessages(context, currentChatId, currentUsername, new RequestCallBack() {
                @Override
                public void onSuccess() {
                    updateMessagesList();
                }
            })).start();
        }
    }

}
