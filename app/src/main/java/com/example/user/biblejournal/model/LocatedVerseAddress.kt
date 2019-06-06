package com.example.user.biblejournal.model

data class LocatedVerseAddress(
        val startIndex: Int,
        val endIndex: Int,
        val verseAddress: VerseAddress
)