package com.example.babble;

import androidx.room.Database;
import androidx.room.RoomDatabase;


import com.example.babble.entities.Message;
import com.example.babble.entities.MessageDao;
import com.example.babble.entities.Contact;
import com.example.babble.entities.ContactDao;
import com.example.babble.entities.Preference;
import com.example.babble.entities.PreferenceDao;

@Database(entities = {Contact.class, Message.class, Preference.class}, version = 12)
public abstract class AppDB extends RoomDatabase {
    public abstract ContactDao contactDao();
    public abstract MessageDao messageDao();

    public abstract PreferenceDao preferenceDao();
}
