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
import java.util.regex.Pattern

class BibleModel(db: AppDb) {
    private val verseDao: VerseDao = db.verseDao()
    private val bookDao: BookDao = db.bookDao()

    private val bookNames: HashMap<String, Int> = HashMap()

    companion object {
        private val verseAddressRegex = ("((?:\\d* ?|[iI]{1,3} )" // number before book // TODO: Fix extra space being matched before bookname
                + "(?:[a-zA-Z]+ ?)+)" // book
                + "(?: ?)" // space after book
                + "(\\d+)"   // chapter
                + "(?: |\\.|:)" // chapter-verse separator
                + "(\\d+)")  // verse
    }

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
        val searchRange = 20
        val searchStart = if (start - searchRange < 0) 0 else start - searchRange
        val searchEnd = if (start + count + searchRange > s.length) s.length else start + count + searchRange
        val searchText = s.subSequence(searchStart, searchEnd).toString()

        val matcher = Pattern.compile(verseAddressRegex).matcher(searchText)
        val transformables = ArrayList<Pair<Int, Int>>()

        while (matcher.find()) {
            if (bookNameHasOneMatch(matcher.group(1))) {
                transformables.add(Pair(searchStart + matcher.start(), searchStart + matcher.end()))
            }
        }

        return transformables
    }

    private fun bookNameHasOneMatch(book: String): Boolean {
        var mBook = book.toLowerCase()
        if (bookNames.containsKey(mBook.toLowerCase())) {
            return true
        }

        var matchCount = 0
        for (bookName in bookNames.keys) {
            if (mBook.length >= 2
                    && bookName.length >= mBook.length
                    && bookName.substring(0, mBook.length).toLowerCase() == mBook) {
                matchCount++
            }
        }

        return matchCount == 1
    }

    fun executeGetVerseById(listener: Repository.VerseRepositoryListener, book: Int, chapter: Int, verse: Int) {
        GetVerseByIdAsyncTask(verseDao, listener).execute(book, chapter, verse)
    }
}
