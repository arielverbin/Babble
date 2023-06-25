package com.example.babble.entities;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MessageDao {
    @Query("SELECT * FROM message")
    List<Message> index();

    @Query("SELECT * FROM message WHERE id = :id")
    Message get(String id);

    @Query("SELECT * FROM message WHERE chatId = :chatId")
    List<Message> getChat(String chatId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Message message);

    @Update
    void update(Message message);

    @Delete
    void delete(Message message);

    @Query("DELETE FROM message WHERE chatId = :chatId")
    void clearChat(String chatId);

    @Query("DELETE FROM message")
    void clearAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Message> messages);
}
