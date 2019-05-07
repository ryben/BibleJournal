package com.example.user.biblejournal.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

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
