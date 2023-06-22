package com.example.babble.contacts;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.babble.registeration.RequestCallBack;

import java.util.List;

public class ContactsViewModel extends AndroidViewModel {

    private final ContactsRepository contactsRepository;
    private final LiveData<List<Contact>> allContacts;

    public ContactsViewModel(@NonNull Application application) {
        super(application);
        contactsRepository = new ContactsRepository(application);
        allContacts = contactsRepository.getAllContacts();
    }

    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }

    public Contact getContactById(String id) {
        return contactsRepository.getContactById(id);
    }

    public Contact getContactByUsername(String username) {
        return contactsRepository.getContactByUsername(username);
    }

    public void insertContact(String username, RequestCallBack callBack) {
        contactsRepository.insertContact(username, callBack);
    }

    public void updateContact(Contact contact, RequestCallBack callBack) {
        contactsRepository.updateContact(contact, callBack);
    }

    public void setLastMessage(String contactId, String lastMes, String timeChatted, RequestCallBack callBack) {
        contactsRepository.updateLastMessage(contactId, lastMes, timeChatted, callBack);
    }

    public void deleteContact(String userId, RequestCallBack callBack) {
        contactsRepository.deleteContact(userId, callBack);
    }
}
