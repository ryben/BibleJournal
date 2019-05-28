package com.example.user.biblejournal.model.database.note;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT id, title, content, dateCreated, dateEdited, state, tags FROM NoteEntity WHERE state = 'ACTIVE'")
    List<NoteEntity> getAllNotes();

    @Insert
    void insertNote(NoteEntity noteEntity);

    @Update
    void updateNote(NoteEntity noteEntity);

    @Query("SELECT id, title, content, dateCreated, dateEdited, state, tags from NoteEntity where id = :id")
    NoteEntity getNoteById(int id);
}
