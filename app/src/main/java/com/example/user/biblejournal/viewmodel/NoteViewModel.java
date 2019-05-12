package com.example.user.biblejournal.viewmodel;


import com.example.user.biblejournal.database.NoteRepository;

import androidx.lifecycle.ViewModel;

public class NoteViewModel extends ViewModel {
    private NoteRepository noteRepository;

    public NoteRepository getNoteRepository() {
        return noteRepository;
    }
}
