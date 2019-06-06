package com.example.user.biblejournal.model

import android.util.Pair
import com.example.user.biblejournal.model.asynctasks.GetVerseByIdAsyncTask
import com.example.user.biblejournal.model.database.AppDb
import com.example.user.biblejournal.model.database.bible.BookDao
import com.example.user.biblejournal.model.database.bible.VerseDao
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class BibleModel(db: AppDb) {
    private val verseDao: VerseDao = db.verseDao()
    private val bookDao: BookDao = db.bookDao()

    private val bookNames: HashMap<String, Int> = HashMap()
    private val verseAddressParser = VerseAddressParser(bookNames)

    init {
        val disposable: Disposable = Observable.fromCallable { bookDao.getAllBooks() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            run {
                                result.forEach { book -> bookNames[book.name.toLowerCase()] = book.id }
                            }
                        }, // onNext
                        {}, // onError
                        {}, // onComplete
                        {} // onSubscribe
                )
    }


    fun findSpannables(s: CharSequence, start: Int, count: Int): List<Pair<Int, Int>> {
        return verseAddressParser.findSpannables(s, start, count)
    }

    fun executeGetVerseById(listener: Repository.VerseRepositoryListener, book: Int, chapter: Int, verse: Int) {
        GetVerseByIdAsyncTask(verseDao, listener).execute(book, chapter, verse)
    }
}
