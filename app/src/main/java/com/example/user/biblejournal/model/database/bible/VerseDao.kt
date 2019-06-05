package com.example.user.biblejournal.model.database.bible

import androidx.room.Dao
import androidx.room.Query

@Dao
interface VerseDao {
    @Query("SELECT book, chapter, verse, content from bible WHERE book=:book AND chapter=:chapter AND verse=:verse")
    fun getVerse(book: Int, chapter: Int, verse: Int): VerseEntity
}
