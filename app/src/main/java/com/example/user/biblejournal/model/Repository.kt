package com.example.user.biblejournal.model

import android.app.Application
import android.util.Pair

import com.example.user.biblejournal.model.asynctasks.GetAllNotesAsyncTask
import com.example.user.biblejournal.model.asynctasks.GetNoteByIdAsyncTask
import com.example.user.biblejournal.model.asynctasks.InsertNoteAsyncTask
import com.example.user.biblejournal.model.asynctasks.UpdateNoteAsyncTask
import com.example.user.biblejournal.model.database.AppDb
import com.example.user.biblejournal.model.database.note.NoteDao
import com.example.user.biblejournal.model.database.note.NoteEntity
import com.example.user.biblejournal.model.database.bible.VerseEntity
import com.example.user.biblejournal.model.note.NoteState

class Repository(app: Application) {
    private val noteDao: NoteDao
    private val bibleModel: BibleModel

    init {
        val db = AppDb.getInstance(app) // TODO: Use dependency injection
        noteDao = db!!.noteDao()
        bibleModel = BibleModel(db)
    }

    interface NotesRepositoryListener {
        fun onNotesRead(noteEntities: List<NoteEntity>)

        fun onNoteFetchedById(noteEntity: NoteEntity)
    }

    interface VerseRepositoryListener {
        fun onVerseRead(verseEntity: VerseEntity)
    }

    fun executeReadAllNotes(notesRepositoryListener: NotesRepositoryListener) { // TODO: Use RxJava
        GetAllNotesAsyncTask(noteDao, notesRepositoryListener).execute()
    }

    fun executeGetNoteById(id: Int, notesRepositoryListener: NotesRepositoryListener) {
        GetNoteByIdAsyncTask(noteDao, notesRepositoryListener).execute(id)
    }

    fun updateNoteState(noteEntity: NoteEntity, newState: NoteState) {
        noteEntity.state = newState.toString()
    }

    fun insertNote(noteEntity: NoteEntity) {
        noteEntity.state = NoteState.ACTIVE.toString()
        InsertNoteAsyncTask(noteDao).execute(noteEntity)
    }

    fun updateNote(noteEntity: NoteEntity) {
        UpdateNoteAsyncTask(noteDao).execute(noteEntity)
    }


    fun findSpannables(s: CharSequence, start: Int, count: Int): List<Pair<Int, Int>> {
        return bibleModel.findSpannables(s, start, count)
    }
}
