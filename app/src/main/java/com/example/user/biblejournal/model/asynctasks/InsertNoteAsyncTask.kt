package com.example.user.biblejournal.model.asynctasks

import android.os.AsyncTask

import com.example.user.biblejournal.model.database.note.NoteDao
import com.example.user.biblejournal.model.database.note.NoteEntity

class InsertNoteAsyncTask(internal val noteDao: NoteDao) : AsyncTask<NoteEntity, Void, Void>() {

    override fun doInBackground(vararg noteEntities: NoteEntity): Void? {
        noteDao.insertNote(noteEntities[0])
        return null
    }
}