package com.example.user.biblejournal.model.note;

import com.example.user.biblejournal.model.database.NoteEntity;

public class NoteFactory {
    public static NoteEntity createNewNote(String title, String content) {
        NoteEntity newNoteEntity = new NoteEntity();

        newNoteEntity.setTitle(title);
        newNoteEntity.setContent(content);
        newNoteEntity.setDateCreated("");
        newNoteEntity.setDateEdited("");

        return newNoteEntity;
    }
}
