package com.example.user.biblejournal.model.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DbImporter {
    private static DbImporter INSTANCE;
    private static final String DB_NAME = "bible_tagalog.db";
    private static final String DB_FOLDER = "databases";
    private static final String TAG = DbImporter.class.getSimpleName();

    private final AppDb appDb;

    public static DbImporter getInstance(Context context) {
        if (null == INSTANCE) {
            INSTANCE = new DbImporter(context);
            return INSTANCE;
        } else {
            return INSTANCE;
        }
    }

    private DbImporter(Context context) {
        copyDbToDataFolder(context, DB_NAME);
        appDb = Room.databaseBuilder(context, AppDb.class, DB_NAME)
                .addMigrations(AppDb.MIGRATION_1_2)
                .build();
    }

    public AppDb getRoomDatabase() {
        return appDb;
    }

    private void copyDbToDataFolder(Context context, String databaseName) {
        final File dbPath = context.getDatabasePath(databaseName);

        if (dbPath.exists()) {
            return;
        }

        // Make sure the path is created
        dbPath.getParentFile().mkdirs();

        try {
            final InputStream inputStream = context.getAssets().open(DB_FOLDER + "/" + databaseName);
            final OutputStream output = new FileOutputStream(dbPath);

            byte[] buffer = new byte[8192];
            int length;

            while ((length = inputStream.read(buffer, 0, 8192)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            inputStream.close();
        } catch (IOException e) {
            Log.d(TAG, "Failed to import database file", e);
            e.printStackTrace();
        }
    }

}