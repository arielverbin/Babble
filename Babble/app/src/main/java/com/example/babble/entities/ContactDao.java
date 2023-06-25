package com.example.babble.entities;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contact")
    List<Contact> index();

    @Query("SELECT * FROM contact WHERE id = :id")
    Contact get(String id);

    @Query("SELECT * FROM contact WHERE username = :username LIMIT 1")
    Contact getByUsername(String username);

    @Insert
    void insert(Contact contact);

    @Update
    void update(Contact contact);

    @Delete
    void delete(Contact contact);

    @Query("DELETE FROM contact WHERE id = :id")
    void deleteById(String id);

    @Query("UPDATE contact SET lastMessage = :content, timeChatted = :timeSent WHERE id = :contactID")
    void setLastMessage(String contactID, String content, String timeSent);

    @Query("DELETE FROM contact")
    void clear();

    @Insert
    void insertAll(List<Contact> contacts);
}
