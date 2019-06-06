package com.example.user.biblejournal.model

import android.util.SparseArray
import java.util.*
import java.util.regex.Pattern

class VerseAddressParser {
    var bookNames = HashMap<String, Int>()
    var maxVerses = SparseArray<List<Int>>()

    companion object {
        private val maxChapters = intArrayOf(50, 40, 27, 36, 34, 24, 21, 4, 31, 24, 22, 25, 29, 36, 10, 13, 10, 42, 150, 31, 12, 8, 66, 52, 5, 48, 12, 14, 3, 9, 1, 4, 7, 3, 3, 3, 2, 14, 4, 28, 16, 24, 21, 28, 16, 16, 13, 6, 6, 4, 4, 5, 3, 6, 4, 3, 1, 13, 5, 5, 3, 5, 1, 1, 1, 22)
        private const val verseAddressRegex =
                ("(?:([iI]{1,3}|\\d+) )?" // number before book
                        + "([a-zA-Z]+(?: [a-zA-Z]+)*)" // book
                        + "[. ]?"      // space after book
                        + "(\\d+)"  // chapter
                        + "[ \\.:]" // chapter-verse separator
                        + "(\\d+)") // verse
    }

    fun findSpannables(s: CharSequence, start: Int, count: Int): List<LocatedVerseAddress> {
        val searchRange = 20

        val searchStart = Math.max(start - searchRange, 0)
        val searchEnd = Math.min(start + count + searchRange, s.length)
        val searchText = s.subSequence(searchStart, searchEnd).toString()

        val matcher = Pattern.compile(verseAddressRegex).matcher(searchText)
        val locatedVerseAddresses = ArrayList<LocatedVerseAddress>()

        while (matcher.find()) {
            val numberBeforeBook = convertToNumberPlusSpace(matcher.group(1) ?: null)
            val bookName = matcher.group(2)
            val chapter = matcher.group(3).toInt()
            val verse = matcher.group(4).toInt()
            val bookMatches = getBookMatches(numberBeforeBook + bookName)

            if (bookMatches.size == 1
                    && chapter <= maxChapters[bookMatches[0] - 1] // check max chapter
                    && verse <= maxVerses[bookMatches[0]]!![chapter - 1] // check max verse
            ) {
                locatedVerseAddresses.add(
                        LocatedVerseAddress(
                                searchStart + matcher.start(),
                                searchStart + matcher.end(),
                                VerseAddress(bookMatches[0], chapter, verse)
                        )
                )
            }
        }

        return locatedVerseAddresses
    }

    private fun convertToNumberPlusSpace(num: String?): String {
        return when {
            num == null -> ""
            num.toIntOrNull() == null -> num.length.toString() + " "
            else -> "$num "
        }
    }

    private fun getBookMatches(book: String): List<Int> {
        val bookMatches = ArrayList<Int>()
        val mBook = book.toLowerCase()

        if (bookNames.containsKey(mBook.toLowerCase())) {
            bookMatches.add(bookNames[mBook.toLowerCase()]!!)
            return bookMatches
        }

        for (bookName in bookNames.keys) {
            if (mBook.length >= 2
                    && bookName.length >= mBook.length
                    && bookName.substring(0, mBook.length).toLowerCase() == mBook) {

                bookMatches.add(bookNames[bookName]!!)
            }
        }

        return bookMatches
    }

}