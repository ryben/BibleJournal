package com.example.user.biblejournal.writer.repository

import android.app.Application
import com.example.user.biblejournal.model.data.LocatedVerseAddress
import com.example.user.biblejournal.model.data.VerseAddress
import com.example.user.biblejournal.model.data.VerseInfo
import com.example.user.biblejournal.database.AppDb
import com.example.user.biblejournal.writer.local.dao.NoteDao
import com.example.user.biblejournal.writer.local.entity.NoteEntity
import com.example.user.biblejournal.model.BibleModel
import com.example.user.biblejournal.model.note.NoteState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class WriterRepository(app: Application) {
    private val noteDao: NoteDao
    private val bibleModel: BibleModel
    private val compositeDisposable = CompositeDisposable()

    init {
        val db = AppDb.getInstance(app) // TODO: Use dependency injection
        noteDao = db!!.noteDao()
        bibleModel = BibleModel(db)
    }

    interface RepositoryListener {
        fun onNotesRead(noteEntities: List<NoteEntity>)

        fun onNoteFetchedById(noteEntity: NoteEntity)

        fun onVerseRead(verseInfo: VerseInfo)
    }

    fun readAllNotes(listener: RepositoryListener) {
        compositeDisposable.add(Observable.fromCallable { noteDao.allNotes }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    run {
                        listener.onNotesRead(result)
                    }
                }, {}))
    }

    fun getNoteById(id: Long, listener: RepositoryListener) {
        compositeDisposable.add(Observable.fromCallable { noteDao.getNoteById(id) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    run {
                        listener.onNoteFetchedById(result)
                    }
                }, {}))
    }

    fun readVerse(verseAddress: VerseAddress, listener: RepositoryListener) {
        bibleModel.getVerseById(verseAddress, listener)
    }

    fun updateNoteState(noteEntity: NoteEntity, newState: NoteState) {
        noteEntity.state = newState.toString()
    }

    fun insertNote(noteEntity: NoteEntity) {
        noteEntity.state = NoteState.ACTIVE.toString()

        compositeDisposable.add(Observable.fromCallable { noteDao.insertNote(noteEntity) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    run {
                    }
                }, {}))
    }

    fun updateNote(noteEntity: NoteEntity) {
        compositeDisposable.add(Observable.fromCallable { noteDao.updateNote(noteEntity) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    run {
                    }
                }, {}))
    }

    fun findSpannables(s: CharSequence, start: Int, count: Int): List<LocatedVerseAddress> {
        return bibleModel.findSpannables(s, start, count)
    }
}
