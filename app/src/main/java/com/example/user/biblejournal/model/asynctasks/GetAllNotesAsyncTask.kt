package com.example.user.biblejournal.model.asynctasks

import android.os.AsyncTask

import com.example.user.biblejournal.model.Repository
import com.example.user.biblejournal.model.database.note.NoteDao
import com.example.user.biblejournal.model.database.note.NoteEntity

class GetAllNotesAsyncTask(private val mAsyncTaskDao: NoteDao, private val notesRepositoryListener: Repository.NotesRepositoryListener) : AsyncTask<Void, Void, List<NoteEntity>>() {

    override fun doInBackground(vararg voids: Void): List<NoteEntity> {
        return mAsyncTaskDao.allNotes
    }

    override fun onPostExecute(noteEntities: List<NoteEntity>) {
        super.onPostExecute(noteEntities)

        notesRepositoryListener.onNotesRead(noteEntities)
    }
}
