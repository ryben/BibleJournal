package com.example.user.biblejournal.model.database;



import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface NoteDao {
    @Query("SELECT id, title FROM NoteEntity")
    LiveData<List<NoteEntity>> getAllNotes();

    @Insert
    void insertNote(NoteEntity noteEntity);
}
