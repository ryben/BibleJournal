package com.example.user.biblejournal.reader.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.user.biblejournal.reader.local.entity.BookNameEntity

@Dao
interface BookNameDao {
    @Query("SELECT id, name FROM bible_tagalog_books")
    fun getAllBookNames() : List<BookNameEntity>
}