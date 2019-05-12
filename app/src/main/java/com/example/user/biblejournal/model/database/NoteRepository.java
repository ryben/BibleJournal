package com.example.user.biblejournal.model.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {
    private final NoteDao noteDao;
    private LiveData<List<NoteEntity>> notes;

    public NoteRepository(Context context) {
        noteDao = NoteDb.getInstance(context).noteDao();
        notes = noteDao.getAllNotes();
    }

    public void insertNote(NoteEntity noteEntity) {
        new InsertNoteAsyncTask(noteDao).execute(noteEntity);
    }

    private static class InsertNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void> {
        final NoteDao noteDao;

        InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteEntity... noteEntities) {
            noteDao.insertNote(noteEntities[0]);
            return null;
        }
    }

    public LiveData<List<NoteEntity>> getNotes() {
        return notes;
    }
}
