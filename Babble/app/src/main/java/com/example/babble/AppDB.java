package com.example.babble;

import androidx.room.Database;
import androidx.room.RoomDatabase;


import com.example.babble.chats.Message;
import com.example.babble.chats.MessageDao;
import com.example.babble.contacts.Contact;
import com.example.babble.contacts.ContactDao;

@Database(entities = {Contact.class, Message.class}, version = 5)
public abstract class AppDB extends RoomDatabase {
    public abstract ContactDao contactDao();
    public abstract MessageDao messageDao();
}
