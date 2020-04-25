package com.example.user.biblejournal.model

import android.util.SparseArray
import com.example.user.biblejournal.model.data.LocatedVerseAddress
import com.example.user.biblejournal.model.data.VerseAddress
import com.example.user.biblejournal.model.data.VerseInfo
import com.example.user.biblejournal.database.AppDb
import com.example.user.biblejournal.reader.local.dao.BookNameDao
import com.example.user.biblejournal.reader.local.dao.MaxVerseDao
import com.example.user.biblejournal.reader.local.dao.VerseDao
import com.example.user.biblejournal.writer.repository.WriterRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class BibleModel(db: AppDb) {
    private val verseDao: VerseDao = db.verseDao()
    private val bookNameDao: BookNameDao = db.bookDao()
    private val maxVerseDao: MaxVerseDao = db.maxVerseDao()

    private val verseAddressParser = VerseAddressParser()
    private val compositeDisposable = CompositeDisposable()

    private val bookNamesToInt = HashMap<String, Int>()
    private val bookNames = SparseArray<String>()

    init {
        val maxVerses = SparseArray<List<Int>>()

        compositeDisposable.add(Observable.fromCallable { maxVerseDao.getMaxVerses() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    run {
                        result.forEach { maxVerse -> maxVerses.put(maxVerse.book, maxVerse.maxVerses.split(",").map { it.toInt() }) }
                        verseAddressParser.maxVerses = maxVerses
                    }
                }, {}))

        compositeDisposable.add(Observable.fromCallable { bookNameDao.getAllBookNames() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    run {
                        result.forEach { book ->
                            run {
                                bookNamesToInt[book.name.toLowerCase()] = book.id
                                bookNames.put(book.id, book.name)
                            }
                        }
                        verseAddressParser.bookNames = bookNamesToInt
                    }
                }, {}))
    }

    fun findSpannables(s: CharSequence, start: Int, count: Int): List<LocatedVerseAddress> {
        return verseAddressParser.findSpannables(s, start, count)
    }

    fun getVerseById(verseAddress: VerseAddress, listener: WriterRepository.RepositoryListener) {
        compositeDisposable.add(Observable.fromCallable { verseDao.getVerse(verseAddress.book, verseAddress.chapter, verseAddress.verse) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    run {

                        val verseInfo = VerseInfo(verseAddress, result.content, bookNames[result.book])

                        listener.onVerseRead(verseInfo)
                    }
                }, {})
        )
    }
}
