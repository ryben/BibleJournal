package com.example.user.biblejournal.model.asynctasks;

import android.os.AsyncTask;

import com.example.user.biblejournal.model.database.note.NoteDao;
import com.example.user.biblejournal.model.database.note.NoteEntity;

public class UpdateNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void> {

    private NoteDao noteDao;

    public UpdateNoteAsyncTask(NoteDao dao) {
        this.noteDao = dao;
    }

    @Override
    protected Void doInBackground(final NoteEntity... params) {
        noteDao.updateNote(params[0]);
        return null;
    }
}
