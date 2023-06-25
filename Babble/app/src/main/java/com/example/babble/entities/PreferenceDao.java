package com.example.babble.entities;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.babble.entities.Preference;

@Dao
public interface PreferenceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void set(Preference preference);

    @Query("SELECT value FROM preference WHERE `key` = :key")
    String get(String key);

    @Query("DELETE FROM Preference WHERE `key` != 'serverUrl'")
    void clear();

}
