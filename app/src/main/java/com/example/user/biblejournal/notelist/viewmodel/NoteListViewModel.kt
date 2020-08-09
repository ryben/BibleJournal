package com.example.user.biblejournal.notelist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.example.user.biblejournal.notelist.repository.NoteListRepository
import com.example.user.biblejournal.writer.local.entity.NoteEntity
import java.util.*

class NoteListViewModel(app: Application) : AndroidViewModel(app), NoteListRepository.RepositoryListener {

    private val noteListRepository: NoteListRepository = NoteListRepository(app)
    val allNotes: MediatorLiveData<List<NoteEntity>> = MediatorLiveData()

    init {
        allNotes.value = ArrayList()
    }

    override fun onNotesRead(noteEntities: List<NoteEntity>) {
        allNotes.value = noteEntities
    }

    fun readAllNotes() {
        noteListRepository.readAllNotes(this)
    }
}