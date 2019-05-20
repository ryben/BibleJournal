package com.example.user.biblejournal.viewmodel;


import android.app.Application;
import android.content.Context;

import com.example.user.biblejournal.model.database.NoteEntity;
import com.example.user.biblejournal.model.database.NoteRepository;
import com.example.user.biblejournal.model.note.NoteState;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository noteRepository;
    private NoteEntity currentNote;

    public NoteViewModel(Application app) {
        super(app);
        noteRepository = new NoteRepository(app);
    }

    public LiveData<List<NoteEntity>> getNotes() {
        return new MutableLiveData<>(noteRepository.getNotes());
    }

    public NoteEntity getCurrentNote() {
        return currentNote;
    }

    public void startNote(@Nullable Integer noteId) {
        if (null == noteId) {
            currentNote = NoteEntity.newInstance();
        } else {
            currentNote = noteRepository.getNoteById(noteId);
        }
    }

    public void saveNote() {
        if (NoteState.NEW.toString().equals(currentNote.getState())) {
            if (!"".equals(currentNote.getTitle()) || !"".equals(currentNote.getContent())) {
                noteRepository.insertNote(currentNote);
            }
        } else {
            noteRepository.updateNote(currentNote);
        }
    }

    public void deleteNote() {
        noteRepository.updateNoteState(currentNote, NoteState.DELETED);
        noteRepository.updateNote(currentNote);
    }

    public void archiveNote() {
        noteRepository.updateNoteState(currentNote, NoteState.ARCHIVED);
        noteRepository.updateNote(currentNote);
    }
}
