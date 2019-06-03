package com.example.user.biblejournal.model.asynctasks

import android.os.AsyncTask

import com.example.user.biblejournal.model.Repository
import com.example.user.biblejournal.model.database.note.NoteDao
import com.example.user.biblejournal.model.database.note.NoteEntity

class GetNoteByIdAsyncTask(private val mAsyncTaskDao: NoteDao, private val notesRepositoryListener: Repository.NotesRepositoryListener) : AsyncTask<Int, Void, NoteEntity>() {

    override fun doInBackground(vararg params: Int?): NoteEntity {
        return mAsyncTaskDao.getNoteById(params[0]!!)
    }

    override fun onPostExecute(noteEntity: NoteEntity) {
        super.onPostExecute(noteEntity)

        notesRepositoryListener.onNoteFetchedById(noteEntity)
    }
}
