package com.example.user.biblejournal.reader.repository

data class LocatedVerseAddress(
        val startIndex: Int,
        val endIndex: Int,
        val verseAddress: VerseAddress
)