package com.example.user.biblejournal.model.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT id, title, content, dateCreated, dateEdited, tags FROM NoteEntity WHERE state = 'ACTIVE'")
    LiveData<List<NoteEntity>> getAllNotes();

    @Insert
    void insertNote(NoteEntity noteEntity);

    @Update
    void updateNote(NoteEntity noteEntity);

    @Query("SELECT id, title, content, tags, dateCreated, dateEdited from NoteEntity where id = :id")
    NoteEntity getNoteById(int id);
}
