package com.example.user.biblejournal.model.asynctasks

import android.os.AsyncTask

import com.example.user.biblejournal.model.database.note.NoteDao
import com.example.user.biblejournal.model.database.note.NoteEntity

class UpdateNoteAsyncTask(private val noteDao: NoteDao) : AsyncTask<NoteEntity, Void, Void>() {

    override fun doInBackground(vararg params: NoteEntity): Void? {
        noteDao.updateNote(params[0])
        return null
    }
}
