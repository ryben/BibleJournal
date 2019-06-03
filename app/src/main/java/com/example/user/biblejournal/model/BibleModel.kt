package com.example.user.biblejournal.model

import android.util.Pair
import android.util.SparseArray

import java.util.ArrayList
import java.util.regex.Matcher
import java.util.regex.Pattern

class BibleModel {
    private var bookNames: SparseArray<String>? = null

    init {
        initBookNames()
    }

    private fun initBookNames() { // TODO: fetch from DB
        bookNames = SparseArray()
        bookNames!!.put(1, "Genesis")
        bookNames!!.put(2, "Exodo")
        bookNames!!.put(3, "Levitico")
        bookNames!!.put(4, "Bilang")
        bookNames!!.put(5, "Deuteronomio")
        bookNames!!.put(6, "Josue")
        bookNames!!.put(7, "Hukom")
        bookNames!!.put(8, "Ruth")
        bookNames!!.put(9, "1 Samuel")
        bookNames!!.put(10, "2 Samuel")
        bookNames!!.put(11, "1 Hari")
        bookNames!!.put(12, "2 Hari")
        bookNames!!.put(13, "1 Cronica")
        bookNames!!.put(14, "2 Cronica")
        bookNames!!.put(15, "Ezra")
        bookNames!!.put(16, "Nehemias")
        bookNames!!.put(17, "Ester")
        bookNames!!.put(18, "Job")
        bookNames!!.put(19, "Awit")
        bookNames!!.put(20, "Kawikaan")
        bookNames!!.put(21, "Eclesiastes")
        bookNames!!.put(22, "Awit ng mga Awit")
        bookNames!!.put(23, "Isaias")
        bookNames!!.put(24, "Jeremias")
        bookNames!!.put(25, "Panaghoy")
        bookNames!!.put(26, "Ezekiel")
        bookNames!!.put(27, "Daniel")
        bookNames!!.put(28, "Oseas")
        bookNames!!.put(29, "Joel")
        bookNames!!.put(30, "Amos")
        bookNames!!.put(31, "Obadias")
        bookNames!!.put(32, "Jonas")
        bookNames!!.put(33, "Mikas")
        bookNames!!.put(34, "Nahum")
        bookNames!!.put(35, "Habakkuk")
        bookNames!!.put(36, "Zefanias")
        bookNames!!.put(37, "Hagai")
        bookNames!!.put(38, "Zacarias")
        bookNames!!.put(39, "Malakias")
        bookNames!!.put(40, "Mateo")
        bookNames!!.put(41, "Marcos")
        bookNames!!.put(42, "Lucas")
        bookNames!!.put(43, "Juan")
        bookNames!!.put(44, "Gawa")
        bookNames!!.put(45, "Roma")
        bookNames!!.put(46, "1 Corinto")
        bookNames!!.put(47, "2 Corinto")
        bookNames!!.put(48, "Galacia")
        bookNames!!.put(49, "Efeso")
        bookNames!!.put(50, "Filipos")
        bookNames!!.put(51, "Colosas")
        bookNames!!.put(52, "1 Tesalonica")
        bookNames!!.put(53, "2 Tesalonica")
        bookNames!!.put(54, "1 Timoteo")
        bookNames!!.put(55, "2 Timoteo")
        bookNames!!.put(56, "Tito")
        bookNames!!.put(57, "Filemon")
        bookNames!!.put(58, "Hebreo")
        bookNames!!.put(59, "Santiago")
        bookNames!!.put(60, "1 Pedro")
        bookNames!!.put(61, "2 Pedro")
        bookNames!!.put(62, "1 Juan")
        bookNames!!.put(63, "2 Juan")
        bookNames!!.put(64, "3 Juan")
        bookNames!!.put(65, "Judas")
        bookNames!!.put(66, "Apocalipsis")
    }

    fun findSpannables(s: CharSequence, start: Int, count: Int): List<Pair<Int, Int>> {

        val searchLength = 10
        var searchStart = start - searchLength
        val searchEnd = start + count

        if (searchStart < 0) {
            searchStart = 0
        }

        val searchText = s.subSequence(searchStart, searchEnd).toString()

        val matcher = Pattern.compile(verseAddressRegex).matcher(searchText)
        val transformables = ArrayList<Pair<Int, Int>>()

        while (matcher.find()) {
            // Do not include if match is up to the last character, to avoid spanning up to the cursor
            // If the last character in the text transformed to span, next typed characters will also be included in the span
            if (searchStart + matcher.end() < start + count) {
                transformables.add(Pair(searchStart + matcher.start(), searchStart + matcher.end()))
            }
        }

        return transformables
    }

    companion object {
        private val verseAddressRegex = ("((?:\\d*|[iI]{1,3}\\s*)[a-zA-Z]+)" // book

                + "(?:\\s*)" // space after book

                + "(\\d+)"   // chapter

                + "(?:\\s+|\\.|:)" // chapter-verse separator

                + "(\\d+)")  // verse
    }
}
