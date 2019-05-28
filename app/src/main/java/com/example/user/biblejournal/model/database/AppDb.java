package com.example.user.biblejournal.model.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.user.biblejournal.model.database.note.NoteDao;
import com.example.user.biblejournal.model.database.note.NoteEntity;
import com.example.user.biblejournal.model.database.verse.VerseDao;
import com.example.user.biblejournal.model.database.verse.VerseEntity;

@Database(entities = {VerseEntity.class, NoteEntity.class}, version = 2)
public abstract class AppDb extends RoomDatabase {
    public abstract NoteDao noteDao();

    public abstract VerseDao verseDao();

    private static AppDb INSTANCE;

    public static AppDb getInstance(Application app) {
        // TODO: Proper way of doing singleton for database
        if (null == INSTANCE) {
            INSTANCE = DbImporter.getInstance(app).getRoomDatabase();
        }
        return INSTANCE;
    }

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };
}
