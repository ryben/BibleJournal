package com.example.user.biblejournal.editnote.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.example.user.biblejournal.model.data.LocatedVerseAddress
import com.example.user.biblejournal.editnote.repository.Repository
import com.example.user.biblejournal.model.data.VerseAddress
import com.example.user.biblejournal.model.data.VerseInfo
import com.example.user.biblejournal.editnote.local.entity.NoteEntity
import com.example.user.biblejournal.model.note.NoteState
import java.util.*

class BibleViewModel(app: Application) : AndroidViewModel(app), Repository.RepositoryListener {
    private val repository: Repository = Repository(app)
    val currentNote: MediatorLiveData<NoteEntity> = MediatorLiveData()
    val allNotes: MediatorLiveData<List<NoteEntity>> = MediatorLiveData()
    val textSpannables: MediatorLiveData<List<LocatedVerseAddress>> = MediatorLiveData()
    val verseToShow = MediatorLiveData<VerseInfo>()

    init {
        allNotes.value = ArrayList()
    }

    fun readAllNotes() {
        repository.readAllNotes(this)
    }

    fun startNote(noteId: Int?) {
        if (null == noteId) {
            currentNote.setValue(NoteEntity.newInstance())
        } else {
            repository.getNoteById(noteId, this)
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

    fun readVerse(verseAddress: VerseAddress) {
        repository.readVerse(verseAddress, this)
    }

    override fun onVerseRead(verseInfo: VerseInfo) {
        verseToShow.value = verseInfo
    }

}
