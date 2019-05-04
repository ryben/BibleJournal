package com.example.user.biblejournal.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT id, title FROM NoteEntity")
    List<NoteEntity> getAllNotes();


}
