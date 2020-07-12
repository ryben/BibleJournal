package com.example.user.biblejournal.notelist.repository

import android.app.Application
import com.example.user.biblejournal.database.AppDb
import com.example.user.biblejournal.writer.local.dao.NoteDao
import com.example.user.biblejournal.writer.local.entity.NoteEntity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NoteListRepository(app: Application) {
    private val noteDao: NoteDao
    private val compositeDisposable = CompositeDisposable()

    init {
        val db = AppDb.getInstance(app) // TODO: Use dependency injection
        noteDao = db!!.noteDao()
    }

    interface RepositoryListener {
        fun onNotesRead(noteEntities: List<NoteEntity>)
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
}