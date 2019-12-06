package com.example.user.biblejournal.reader.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.user.biblejournal.reader.local.entity.MaxVerseEntity

@Dao
interface MaxVerseDao {
    @Query("SELECT  book, max_verses FROM max_verse")
    fun getMaxVerses() : List<MaxVerseEntity>
}