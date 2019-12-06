package com.example.user.biblejournal.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.user.biblejournal.reader.local.dao.BookNameDao;
import com.example.user.biblejournal.reader.local.entity.BookNameEntity;
import com.example.user.biblejournal.reader.local.dao.MaxVerseDao;
import com.example.user.biblejournal.reader.local.entity.MaxVerseEntity;
import com.example.user.biblejournal.reader.local.dao.VerseDao;
import com.example.user.biblejournal.reader.local.entity.VerseEntity;
import com.example.user.biblejournal.database.note.NoteDao;
import com.example.user.biblejournal.database.note.NoteEntity;

@Database(entities = {VerseEntity.class, NoteEntity.class, BookNameEntity.class, MaxVerseEntity.class}, version = 2)
public abstract class AppDb extends RoomDatabase {
    public abstract NoteDao noteDao();

    public abstract VerseDao verseDao();

    public abstract BookNameDao bookDao();

    public abstract MaxVerseDao maxVerseDao();

    private static AppDb INSTANCE;

    public static AppDb getInstance(Application app) {
        // TODO: Proper way of doing singleton for database
        if (null == INSTANCE) {
            INSTANCE = DbImporter.getInstance(app).getRoomDatabase();
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };
}
