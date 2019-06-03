package com.example.user.biblejournal.model.database.verse

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["book", "chapter", "verse"], tableName = "bible")
class VerseEntity {
    @NonNull
    @ColumnInfo
    var book: Int? = null

    @NonNull
    @ColumnInfo
    var chapter: Int? = null

    @NonNull
    @ColumnInfo
    var verse: Int? = null

    @ColumnInfo
    var content: String = ""
}
