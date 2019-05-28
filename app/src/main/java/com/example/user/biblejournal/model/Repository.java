package com.example.user.biblejournal.model;

import android.app.Application;
import android.os.AsyncTask;

import com.example.user.biblejournal.model.database.AppDb;
import com.example.user.biblejournal.model.database.note.NoteDao;
import com.example.user.biblejournal.model.database.note.NoteEntity;
import com.example.user.biblejournal.model.database.verse.VerseDao;
import com.example.user.biblejournal.model.database.verse.VerseEntity;
import com.example.user.biblejournal.model.note.NoteState;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Repository {
    private final NoteDao noteDao;
    private final VerseDao verseDao;

    // TODO: Proper achitecture of Repository
    public Repository(Application app) {
        AppDb db = AppDb.getInstance(app); // TODO: Use dependency injection
        noteDao = db.noteDao();
        verseDao = db.verseDao();
    }

    public interface NotesRepositoryListener {
        void onNotesRead(List<NoteEntity> noteEntities);

        void onNoteFetchedById(NoteEntity noteEntity);
    }

    public void executeReadAllNotes(NotesRepositoryListener notesRepositoryListener) {
        new GetAllNotesAsyncTask(noteDao, notesRepositoryListener).execute();
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

    // TODO: Use callback functions or RxJava

    public void executeGetNoteById(int id, NotesRepositoryListener notesRepositoryListener) {
        new GetNoteByIdAsyncTask(noteDao, notesRepositoryListener).execute(id);
    }

    public VerseEntity getVerseById(int book, int chapter, int verse) {
        VerseEntity verseEntity = null;
        try {
            verseEntity = new GetVerseByIdAsyncTask(verseDao).execute(book, chapter, verse).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace(); // TODO: Proper error handling
        }
        return verseEntity;
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

    private static class GetAllNotesAsyncTask extends AsyncTask<Void, Void, List<NoteEntity>> {
        private NoteDao mAsyncTaskDao;
        private NotesRepositoryListener notesRepositoryListener;

        GetAllNotesAsyncTask(NoteDao dao, NotesRepositoryListener notesRepositoryListener) {
            mAsyncTaskDao = dao;
            this.notesRepositoryListener = notesRepositoryListener;
        }

        @Override
        protected List<NoteEntity> doInBackground(Void... voids) {
            return mAsyncTaskDao.getAllNotes();
        }

        @Override
        protected void onPostExecute(List<NoteEntity> noteEntities) {
            super.onPostExecute(noteEntities);

            notesRepositoryListener.onNotesRead(noteEntities);
        }
    }

    private static class GetNoteByIdAsyncTask extends AsyncTask<Integer, Void, NoteEntity> {

        private NoteDao mAsyncTaskDao;
        private NotesRepositoryListener notesRepositoryListener;

        GetNoteByIdAsyncTask(NoteDao dao, NotesRepositoryListener notesRepositoryListener) {
            mAsyncTaskDao = dao;
            this.notesRepositoryListener = notesRepositoryListener;
        }

        @Override
        protected NoteEntity doInBackground(final Integer... params) {
            return mAsyncTaskDao.getNoteById(params[0]);
        }

        @Override
        protected void onPostExecute(NoteEntity noteEntity) {
            super.onPostExecute(noteEntity);

            notesRepositoryListener.onNoteFetchedById(noteEntity);
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
}
