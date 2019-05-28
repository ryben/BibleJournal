package com.example.user.biblejournal.model.database.verse;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface VerseDao {
    @Query("SELECT book, chapter, verse, content from bible WHERE book=:book AND chapter=:chapter AND verse=:verse")
    VerseEntity getVerse(int book, int chapter, int verse);
}
