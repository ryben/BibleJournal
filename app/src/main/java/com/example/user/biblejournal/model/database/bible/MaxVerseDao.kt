package com.example.user.biblejournal.model.database.bible

import androidx.room.Dao
import androidx.room.Query

@Dao
interface MaxVerseDao {
    @Query("SELECT  book, max_verses FROM max_verse")
    fun getMaxVerses() : List<MaxVerseEntity>
}