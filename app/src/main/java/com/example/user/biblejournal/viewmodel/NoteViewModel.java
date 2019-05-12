package com.example.user.biblejournal.viewmodel;


import android.content.Context;

import com.example.user.biblejournal.model.database.NoteEntity;
import com.example.user.biblejournal.model.database.NoteRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class NoteViewModel extends ViewModel {
    private NoteRepository noteRepository;

    public void init(Context context) {
        noteRepository = new NoteRepository(context);
    }


    public NoteRepository getNoteRepository() {
        return noteRepository;
    }

    public LiveData<List<NoteEntity>> getNotes() {
        return noteRepository.getNotes();
    }
}
