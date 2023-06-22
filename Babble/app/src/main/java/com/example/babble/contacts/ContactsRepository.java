package com.example.babble.contacts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.babble.API.ContactsAPI;
import com.example.babble.AppDB;
import com.example.babble.MyApplication;
import com.example.babble.R;
import com.example.babble.registeration.RequestCallBack;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ContactsRepository {

    private final ContactDao contactDao;

    private final Context context;
    private final ContactsAPI api;
    private final MutableLiveData<List<Contact>> contactListData;

    public ContactsRepository(ContextWrapper context) {
        AppDB db = Room.databaseBuilder(context, AppDB.class, "AppDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        contactDao = db.contactDao();

        api = new ContactsAPI();
        contactListData = new ContactsListData();

        this.context = context;

        updateContactList();
    }

    public LiveData<List<Contact>> getAllContacts() {
        return contactListData;
    }

    public Contact getContactById(String id) {
        return contactDao.get(id);
    }

    public Contact getContactByUsername(String username) {
        return contactDao.getByUsername(username);
    }

    public void insertContact(String username, RequestCallBack callback) {
        new Thread(() -> {
            api.addContact(MyApplication.context, username, new RequestCallBack() {
                // success! update contact list, notifying caller with "success"
                @Override
                public void onSuccess() {
                    updateContactList();
                    callback.onSuccess();
                }
                // notifying caller with "failure"
                @Override
                public void onFailure(String error) {
                    callback.onFailure(error);
                }
            });
        }).start();
    }

    public void updateContact(Contact contact, RequestCallBack callBack) {
        new Thread(() -> {
            contactDao.update(contact);
            updateContactList();
        }).start();
    }

    public void deleteContact(String usernameId, RequestCallBack callBack) {
        new Thread(() -> {
            api.deleteContact(MyApplication.context, usernameId, new RequestCallBack() {
                // success! update contact list and notify caller with success.
                @Override
                public void onSuccess() {
                    updateContactList();
                    callBack.onSuccess();
                }
                // notify caller with failure.
                @Override
                public void onFailure(String error) {
                    callBack.onFailure(error);
                }
            });
        }).start();
    }

    public void updateLastMessage(String contactId, String lastMes, String timeChatted, RequestCallBack callBack) {
        new Thread(() -> {
            contactDao.setLastMessage(contactId, lastMes, timeChatted);
            updateContactList();
        }).start();
    }

    private void updateContactList() {
        List<Contact> contacts = contactDao.index();
        contactListData.postValue(contacts);
    }


    class ContactsListData extends MutableLiveData<List<Contact>> {

        public ContactsListData() {
            super();
            setValue(contactDao.index());
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
                api.getContacts(context, new RequestCallBack() {
                    @Override
                    public void onSuccess() {
                        updateContactList();
                    }
                });

            }).start();
        }
    }
}
