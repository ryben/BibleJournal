package com.example.user.biblejournal.model.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.user.biblejournal.model.note.NoteState;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class NoteRepository {
    private final NoteDao noteDao;
    private final VerseDao verseDao;
    private LiveData<List<NoteEntity>> notes;

    // TODO: Proper achitecture of Repository
    public NoteRepository(Context context) {
        AppDb db = AppDb.getInstance(context);
        noteDao = db.noteDao();
        verseDao = db.verseDao();
        notes = noteDao.getAllNotes();
    }

    public void updateNoteState(NoteEntity noteEntity, NoteState newState) {
        noteEntity.setState(newState.toString());
    }

    public void insertNote(NoteEntity noteEntity) {
        noteEntity.setState(NoteState.ACTIVE.toString());
        new InsertNoteAsyncTask(noteDao).execute(noteEntity);
    }

    public void updateNote(NoteEntity noteEntity) {
        new UpdateNoteAsyncTask(noteDao).execute(noteEntity);
    }

    public NoteEntity getNoteById(int id) {
        NoteEntity noteEntity;
        try {
            noteEntity = new GetNoteByIdAsyncTask(noteDao).execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace(); // TODO: Proper error handling
            throw new RuntimeException("Failed to fetch note with id " + id);
        }
        return noteEntity;
    }

    public VerseEntity getVerseById(int book, int chapter, int verse) {
        VerseEntity VerseEntity;
        try {
            VerseEntity = new GetVerseByIdAsyncTask(verseDao).execute(book, chapter, verse).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace(); // TODO: Proper error handling
            throw new RuntimeException("Failed to fetch verse with Book:" + book + ", Chapter:" + chapter + ", Verse:" + verse);
        }
        return VerseEntity;
    }

    private static class GetVerseByIdAsyncTask extends AsyncTask<Integer, Void, VerseEntity> {

        private VerseDao mAsyncTaskDao;

        GetVerseByIdAsyncTask(VerseDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected VerseEntity doInBackground(final Integer... params) {
            return mAsyncTaskDao.getVerse(params[0], params[1], params[2]);
        }
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

    private static class UpdateNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void> {

        private NoteDao noteDao;

        UpdateNoteAsyncTask(NoteDao dao) {
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
