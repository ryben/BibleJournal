package com.example.user.biblejournal.reader.repository

data class VerseInfo(
        val verseAddress: VerseAddress,
        val content: String,
        val bookName: String
)