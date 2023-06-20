package com.example.babble.contacts;

import android.content.ContextWrapper;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.babble.AppDB;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ContactsRepository {

    private final ContactDao contactDao;
    private final MutableLiveData<List<Contact>> contactListData;

    public ContactsRepository(ContextWrapper context) {
        AppDB db = Room.databaseBuilder(context, AppDB.class, "AppDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        contactDao = db.contactDao();
        contactListData = new ContactsListData();

        updateContactList();
    }

    public LiveData<List<Contact>> getAllContacts() {
        return contactListData;
    }

    public Contact getContactById(int id) {
        return contactDao.get(id);
    }

    public Contact getContactByUsername(String username) {
        return contactDao.getByUsername(username);
    }

    public void insertContact(Contact contact) {
        Log.d("UPDATECONTACT", "called for insertion IN viewmodel");
        new Thread(() -> {
            contactDao.insert(contact);
            updateContactList();
        }).start();
    }

    public void updateContact(Contact contact) {
        new Thread(() -> {
            contactDao.update(contact);
            updateContactList();
        }).start();
    }

    public void deleteContact(Contact contact) {
        new Thread(() -> {
            contactDao.delete(contact);
            updateContactList();
        }).start();
    }

    public void deleteContactById(int id) {
        new Thread(() -> {
            contactDao.deleteById(id);
            updateContactList();
        }).start();
    }

    public void updateLastMessage(int contactId, String lastMes, String timeChatted) {
        new Thread(()->{
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
            setValue(new LinkedList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();
            new Thread(() -> contactListData.postValue(contactDao.index())).start();
        }
    }

}
