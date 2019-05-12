package com.example.user.biblejournal.database;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NoteEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @ColumnInfo
    public String title;

    @ColumnInfo
    public String content;

    @ColumnInfo
    public String dateCreated;

    @ColumnInfo
    public String dateEdited;

}
