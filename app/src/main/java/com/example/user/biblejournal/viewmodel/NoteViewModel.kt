package com.example.user.biblejournal.viewmodel


import android.app.Application
import android.util.Pair
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

import com.example.user.biblejournal.model.Repository
import com.example.user.biblejournal.model.database.note.NoteEntity
import com.example.user.biblejournal.model.database.verse.VerseEntity
import com.example.user.biblejournal.model.note.NoteState

import java.util.ArrayList
import java.util.Objects

class NoteViewModel(app: Application) : AndroidViewModel(app), Repository.NotesRepositoryListener, Repository.VerseRepositoryListener {
    private val repository: Repository
    val currentNote: MediatorLiveData<NoteEntity>
    private val allNotes: MediatorLiveData<List<NoteEntity>>
    val textSpannables: MediatorLiveData<List<Pair<Int, Int>>>

    init {
        repository = Repository(app)

        val noteEntities = ArrayList<NoteEntity>()
        allNotes = MediatorLiveData()
        allNotes.value = noteEntities

        currentNote = MediatorLiveData()
        textSpannables = MediatorLiveData()
    }

    fun getAllNotes(): LiveData<List<NoteEntity>> {
        return allNotes
    }

    fun readAllNotes() {
        repository.executeReadAllNotes(this)
    }

    fun startNote(noteId: Int?) {
        if (null == noteId) {
            currentNote.setValue(NoteEntity.newInstance())
        } else {
            repository.executeGetNoteById(noteId, this)
        }
    }

    override fun onNotesRead(noteEntities: List<NoteEntity>) {
        allNotes.value = noteEntities
    }

    override fun onNoteFetchedById(noteEntity: NoteEntity) {
        currentNote.value = noteEntity
    }

    fun saveNote() {
        if (NoteState.NEW.toString() == Objects.requireNonNull<NoteEntity>(currentNote.value).state) {
            if ("" != currentNote.value!!.title || "" != currentNote.value!!.content) {
                repository.insertNote(currentNote.value!!)
            }
        } else {
            repository.updateNote(currentNote.value!!)
        }
    }

    fun deleteNote() {
        repository.updateNoteState(currentNote.value!!, NoteState.DELETED)
        repository.updateNote(currentNote.value!!)
    }

    fun archiveNote() {
        repository.updateNoteState(currentNote.value!!, NoteState.ARCHIVED)
        repository.updateNote(currentNote.value!!)
    }


    fun findSpannables(s: CharSequence, start: Int, count: Int) { // TODO: To Move to model layer
        val spannables = repository.findSpannables(s, start, count)
        if (!spannables.isEmpty()) {
            textSpannables.value = spannables
        }
    }

    override fun onVerseRead(verseEntity: VerseEntity) {
        // TODO: Implement onVerseRead
    }
}
