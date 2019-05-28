package com.example.user.biblejournal.model.asynctasks;

import android.os.AsyncTask;

import com.example.user.biblejournal.model.Repository;
import com.example.user.biblejournal.model.database.note.NoteDao;
import com.example.user.biblejournal.model.database.note.NoteEntity;

public class GetNoteByIdAsyncTask extends AsyncTask<Integer, Void, NoteEntity> {

    private NoteDao mAsyncTaskDao;
    private Repository.NotesRepositoryListener notesRepositoryListener;

    public GetNoteByIdAsyncTask(NoteDao dao, Repository.NotesRepositoryListener notesRepositoryListener) {
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
