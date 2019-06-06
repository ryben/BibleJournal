package com.example.user.biblejournal.model

import android.util.Pair
import java.util.*
import java.util.regex.Pattern

class VerseAddressParser(private var bookNames: HashMap<String, Int>) {

    companion object {
        private const val verseAddressRegex =
                ("(?:([iI]{1,3}|\\d+) )?" // number before book
                        + "([a-zA-Z]+(?: [a-zA-Z]+)*)" // book
                        + " ?"      // space after book
                        + "(\\d+)"  // chapter
                        + "[ \\.:]" // chapter-verse separator
                        + "(\\d+)") // verse
    }


    fun findSpannables(s: CharSequence, start: Int, count: Int): List<Pair<Int, Int>> {
        val searchRange = 20

        val searchStart = Math.max(start - searchRange, 0)
        val searchEnd = Math.min(start + count + searchRange, s.length)
        val searchText = s.subSequence(searchStart, searchEnd).toString()

        val matcher = Pattern.compile(verseAddressRegex).matcher(searchText)
        val transformables = ArrayList<Pair<Int, Int>>()

        while (matcher.find()) {
            val bookNumber = convertToNumberPlusSpace(matcher.group(1) ?: null)
            val bookName = matcher.group(2)

            if (bookNameHasOneMatch(bookNumber + bookName)) {
                transformables.add(Pair(searchStart + matcher.start(), searchStart + matcher.end()))
            }
        }

        return transformables
    }

    private fun convertToNumberPlusSpace(num: String?): String {
        return when {
            num == null -> ""
            num.toIntOrNull() == null -> num.length.toString() + " "
            else -> "$num "
        }
    }


    private fun bookNameHasOneMatch(book: String): Boolean {
        val mBook = book.toLowerCase()
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

}