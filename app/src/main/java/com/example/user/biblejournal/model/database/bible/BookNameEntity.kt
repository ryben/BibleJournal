package com.example.user.biblejournal.model.database.bible

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bible_tagalog_books")
data class BookNameEntity (
    @NonNull
    @PrimaryKey
    var id : Int,

    @NonNull
    @ColumnInfo
    var name : String
)