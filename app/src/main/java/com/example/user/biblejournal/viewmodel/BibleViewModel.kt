package com.example.user.biblejournal.viewmodel


import android.app.Application
import android.util.Pair
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.example.user.biblejournal.model.LocatedVerseAddress
import com.example.user.biblejournal.model.Repository
import com.example.user.biblejournal.model.database.bible.VerseEntity
import com.example.user.biblejournal.model.database.note.NoteEntity
import com.example.user.biblejournal.model.note.NoteState
import java.util.*

class BibleViewModel(app: Application) : AndroidViewModel(app), Repository.NotesRepositoryListener, Repository.VerseRepositoryListener {
    private val repository: Repository = Repository(app)
    val currentNote: MediatorLiveData<NoteEntity> = MediatorLiveData()
    val allNotes: MediatorLiveData<List<NoteEntity>> = MediatorLiveData()
    val textSpannables: MediatorLiveData<List<LocatedVerseAddress>> = MediatorLiveData()

    init {
        allNotes.value = ArrayList()
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
        if (NoteState.NEW.toString() == currentNote.value!!.state) {
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


    fun findSpannables(s: CharSequence, start: Int, count: Int) {
        val spannables = repository.findSpannables(s, start, count)
        if (spannables.isNotEmpty()) {
            textSpannables.value = spannables
        }
    }

    fun clearSpannables() {
        textSpannables.value = emptyList()
    }

    override fun onVerseRead(verseEntity: VerseEntity) {
        // TODO: Implement onVerseRead
    }
}
