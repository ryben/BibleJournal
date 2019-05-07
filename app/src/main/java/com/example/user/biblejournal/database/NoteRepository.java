package com.example.user.biblejournal.database;

import android.content.Context;
import android.os.AsyncTask;

public class NoteRepository {
    private final NoteDao noteDao;

    public NoteRepository(Context context) {
        noteDao = NoteDb.getInstance(context).noteDao();
    }

    public void insertNote(NoteEntity noteEntity) {
        new InsertNoteAsyncTask(noteDao).doInBackground(noteEntity);
    }

    private class InsertNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void> {
        final NoteDao noteDao;

        public InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteEntity... noteEntities) {
            noteDao.insertNote(noteEntities[0]);
            return null;
        }
    }
}
