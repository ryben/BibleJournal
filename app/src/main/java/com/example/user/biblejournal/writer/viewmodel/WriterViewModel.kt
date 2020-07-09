package com.example.user.biblejournal.writer.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.example.user.biblejournal.model.data.LocatedVerseAddress
import com.example.user.biblejournal.writer.repository.WriterRepository
import com.example.user.biblejournal.model.data.VerseAddress
import com.example.user.biblejournal.model.data.VerseInfo
import com.example.user.biblejournal.writer.local.entity.NoteEntity
import com.example.user.biblejournal.model.note.NoteState
import java.util.*

class WriterViewModel(app: Application) : AndroidViewModel(app), WriterRepository.RepositoryListener {
    private val writerRepository: WriterRepository = WriterRepository(app)
    val currentNote: MediatorLiveData<NoteEntity> = MediatorLiveData()
    val allNotes: MediatorLiveData<List<NoteEntity>> = MediatorLiveData()

    val textSpannables: MediatorLiveData<List<LocatedVerseAddress>> = MediatorLiveData()
    val verseToShow = MediatorLiveData<VerseInfo>()

    init {
        allNotes.value = ArrayList()
    }

    fun readAllNotes() {
        writerRepository.readAllNotes(this)
    }

    fun startNote(noteId: Int?) {
        if (null == noteId) {
            currentNote.setValue(NoteEntity.newInstance())
        } else {
            writerRepository.getNoteById(noteId, this)
        }
    }

    override fun onNotesRead(noteEntities: List<NoteEntity>) {
        allNotes.value = noteEntities
    }

    override fun onNoteFetchedById(noteEntity: NoteEntity) {
        currentNote.value = noteEntity
    }

    fun saveNote() {
        if (currentNote.value == null) {
            // TODO: Error handling
        } else if (NoteState.NEW.toString() == currentNote.value?.state) {
            if ("" != currentNote.value!!.title || "" != currentNote.value!!.content) {
                writerRepository.insertNote(currentNote.value!!)
            }
        } else {
            writerRepository.updateNote(currentNote.value!!)
        }
    }

    fun deleteNote() {
        writerRepository.updateNoteState(currentNote.value!!, NoteState.DELETED)
        writerRepository.updateNote(currentNote.value!!)
    }

    fun archiveNote() {
        writerRepository.updateNoteState(currentNote.value!!, NoteState.ARCHIVED)
        writerRepository.updateNote(currentNote.value!!)
    }


    fun findSpannables(s: CharSequence, start: Int, count: Int) {
        val spannables = writerRepository.findSpannables(s, start, count)
        if (spannables.isNotEmpty()) {
            textSpannables.value = spannables
        }
    }

    fun clearSpannables() {
        textSpannables.value = emptyList()
    }

    fun readVerse(verseAddress: VerseAddress) {
        writerRepository.readVerse(verseAddress, this)
    }

    override fun onVerseRead(verseInfo: VerseInfo) {
        verseToShow.value = verseInfo
    }

}
