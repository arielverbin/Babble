package com.example.babble.contacts;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
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

    public Contact getContactById(int id) {
        return contactsRepository.getContactById(id);
    }

    public Contact getContactByUsername(String username) {
        return contactsRepository.getContactByUsername(username);
    }

    public void insertContact(Contact contact) {
        contactsRepository.insertContact(contact);
    }

    public void updateContact(Contact contact) {
        contactsRepository.updateContact(contact);
    }

    public void setLastMessage(int contactId, String lastMes, String timeChatted) {
        contactsRepository.updateLastMessage(contactId, lastMes, timeChatted);
    }

    public void deleteContact(Contact contact) {
        contactsRepository.deleteContact(contact);
    }

    public void deleteContactById(int id) {
        contactsRepository.deleteContactById(id);
    }
}
