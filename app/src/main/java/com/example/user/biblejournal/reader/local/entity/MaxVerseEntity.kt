package com.example.user.biblejournal.reader.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "max_verse")
data class MaxVerseEntity(
        @NonNull
        @PrimaryKey
        var book : Int,

        @NonNull
        @ColumnInfo(name = "max_verses")
        var maxVerses : String
)