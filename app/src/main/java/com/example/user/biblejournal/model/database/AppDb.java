package com.example.user.biblejournal.model.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {VerseEntity.class, NoteEntity.class}, version = 2)
public abstract class AppDb extends RoomDatabase {
    public abstract NoteDao noteDao();

    public abstract VerseDao verseDao();

    private static AppDb INSTANCE;

    public static AppDb getInstance(Context context) {
        // TODO: Proper way of doing singleton for database
        if (null == INSTANCE) {
            INSTANCE = DbImporter.getInstance(context).getRoomDatabase();
        }
        return INSTANCE;
    }

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };
}
