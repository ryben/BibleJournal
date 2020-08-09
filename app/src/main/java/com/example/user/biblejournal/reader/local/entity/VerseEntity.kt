package com.example.user.biblejournal.reader.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["book", "chapter", "verse"], tableName = "bible")
data class VerseEntity(
        @NonNull
        @ColumnInfo
        var book: Int,

        @NonNull
        @ColumnInfo
        var chapter: Int,

        @NonNull
        @ColumnInfo
        var verse: Int,

        @ColumnInfo
        var content: String = ""
)
