package com.example.user.biblejournal.model.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NoteEntity.class}, version = 1)
public abstract class NoteDb extends RoomDatabase {
    public abstract NoteDao noteDao();

    public static NoteDb INSTANCE;
    public static final String DATABASE_NAME = "bible_journal_db";

    public static NoteDb getInstance(Context context) {
        // TODO: Proper way of doing singleton for database
        if (null == INSTANCE) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), NoteDb.class, DATABASE_NAME).build();
        }
        return INSTANCE;
    }
}
