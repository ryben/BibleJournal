package com.example.user.biblejournal.model.database.verse;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"book", "chapter", "verse"}, tableName = "bible")
public class VerseEntity {
    @NonNull
    @ColumnInfo
    public Integer book;

    @NonNull
    @ColumnInfo
    public Integer chapter;

    @NonNull
    @ColumnInfo
    public Integer verse;

    @ColumnInfo
    public String content;

    public Integer getBook() {
        return book;
    }

    public void setBook(Integer book) {
        this.book = book;
    }

    public Integer getChapter() {
        return chapter;
    }

    public void setChapter(Integer chapter) {
        this.chapter = chapter;
    }

    public Integer getVerse() {
        return verse;
    }

    public void setVerse(Integer verse) {
        this.verse = verse;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
