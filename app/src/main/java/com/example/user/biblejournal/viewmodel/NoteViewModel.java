package com.example.user.biblejournal.viewmodel;


import android.content.Context;

import com.example.user.biblejournal.model.database.NoteEntity;
import com.example.user.biblejournal.model.database.NoteRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class NoteViewModel extends ViewModel {
    private NoteRepository noteRepository;
    private NoteEntity currentNote;

    public void init(Context context) {
        noteRepository = new NoteRepository(context);
    }

    public NoteRepository getNoteRepository() {
        return noteRepository;
    }

    public LiveData<List<NoteEntity>> getNotes() {
        return noteRepository.getNotes();
    }

    public NoteEntity getCurrentNote() {
        return currentNote;
    }

    public void setCurrentNote(NoteEntity currentNote) {
        this.currentNote = currentNote;
    }

    public NoteEntity queryNoteById(int noteId) {
        return noteRepository.getNoteById(noteId);
    }
}
