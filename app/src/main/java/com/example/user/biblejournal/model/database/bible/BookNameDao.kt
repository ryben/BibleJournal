package com.example.user.biblejournal.model.database.bible

import androidx.room.Dao
import androidx.room.Query

@Dao
interface BookNameDao {
    @Query("SELECT id, name FROM bible_tagalog_books")
    fun getAllBookNames() : List<BookNameEntity>
}