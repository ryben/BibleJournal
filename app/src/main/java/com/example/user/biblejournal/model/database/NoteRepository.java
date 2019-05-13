package com.example.user.biblejournal.model.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

    public void updateNote(NoteEntity noteEntity) {
        new UpdateAsyncTask(noteDao).execute(noteEntity);
    }

    public NoteEntity getNoteById(int id) {
        NoteEntity noteEntity = null;
        try {
            noteEntity = new GetNoteByIdAsyncTask(noteDao).execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace(); // TODO: Proper error handling
            throw new RuntimeException("Failed to fetch note with id " + id);
        }
        return noteEntity;
    }

    private static class GetNoteByIdAsyncTask extends AsyncTask<Integer, Void, NoteEntity> {

        private NoteDao mAsyncTaskDao;

        GetNoteByIdAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected NoteEntity doInBackground(final Integer... params) {
            return mAsyncTaskDao.getNoteById(params[0]);
        }
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

    private static class UpdateAsyncTask extends AsyncTask<NoteEntity, Void, Void> {

        private NoteDao noteDao;

        UpdateAsyncTask(NoteDao dao) {
            this.noteDao = dao;
        }

        @Override
        protected Void doInBackground(final NoteEntity... params) {
            noteDao.updateNote(params[0]);
            return null;
        }
    }

    public LiveData<List<NoteEntity>> getNotes() {
        return notes;
    }
}
