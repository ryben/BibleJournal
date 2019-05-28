package com.example.user.biblejournal.model.asynctasks;

import android.os.AsyncTask;

import com.example.user.biblejournal.model.database.note.NoteDao;
import com.example.user.biblejournal.model.database.note.NoteEntity;

public class InsertNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void> {
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