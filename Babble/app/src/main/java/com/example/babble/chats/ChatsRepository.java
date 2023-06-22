package com.example.babble.chats;

import android.content.ContextWrapper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.babble.AppDB;


import java.util.LinkedList;
import java.util.List;

public class ChatsRepository {

    private final MessageDao messageDao;
    private final int currentChatId;
    private final MutableLiveData<List<Message>> messagesListData;

    public ChatsRepository(ContextWrapper context, int chatId) {
        this.currentChatId = chatId;

        AppDB db = Room.databaseBuilder(context, AppDB.class, "AppDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        messageDao = db.messageDao();
        messagesListData = new MessagesListData();

        updateMessagesList();
    }

    public LiveData<List<Message>> getAllMessages() {
        return messagesListData;
    }

    public Message getMessage(int id) {
        return messageDao.get(id);
    }

    public void insertMessage(Message message) {
        new Thread(() -> {
            messageDao.insert(message);
            updateMessagesList();
        }).start();
    }

    public void updateMessage(Message message) {
        new Thread(() -> {
            messageDao.update(message);
            updateMessagesList();
        }).start();
    }

    public void deleteMessage(Message message) {
        new Thread(() -> {
            messageDao.delete(message);
            updateMessagesList();
        }).start();
    }

    private void updateMessagesList() {
        List<Message> messages = messageDao.getChat(currentChatId);
        messagesListData.postValue(messages);
    }


    class MessagesListData extends MutableLiveData<List<Message>> {

        public MessagesListData() {
            super();
            setValue(new LinkedList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();
            new Thread(() -> messagesListData.postValue(messageDao.getChat(currentChatId))).start();
        }
    }

}
