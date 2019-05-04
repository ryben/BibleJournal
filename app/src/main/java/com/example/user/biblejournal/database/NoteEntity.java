package com.example.user.biblejournal.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

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
