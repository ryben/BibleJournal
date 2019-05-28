package com.example.user.biblejournal.model.asynctasks;

import android.os.AsyncTask;

import com.example.user.biblejournal.model.Repository;
import com.example.user.biblejournal.model.database.note.NoteDao;
import com.example.user.biblejournal.model.database.note.NoteEntity;

import java.util.List;

public class GetAllNotesAsyncTask extends AsyncTask<Void, Void, List<NoteEntity>> {
    private NoteDao mAsyncTaskDao;
    private Repository.NotesRepositoryListener notesRepositoryListener;

    public GetAllNotesAsyncTask(NoteDao dao, Repository.NotesRepositoryListener notesRepositoryListener) {
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
