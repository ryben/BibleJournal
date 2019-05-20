package com.example.user.biblejournal.viewmodel;


import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.user.biblejournal.model.database.NoteEntity;
import com.example.user.biblejournal.model.database.NoteRepository;
import com.example.user.biblejournal.model.note.NoteState;

import java.util.ArrayList;
import java.util.List;

public class NoteViewModel extends AndroidViewModel implements NoteRepository.NotesRepositoryListener {
    private NoteRepository noteRepository;
    private NoteEntity currentNote;
    private MediatorLiveData<List<NoteEntity>> allNotes;

    public NoteViewModel(Application app) {
        super(app);
        noteRepository = new NoteRepository(app);

        List<NoteEntity> noteEntities = new ArrayList<>();
        allNotes = new MediatorLiveData<>();
        allNotes.setValue(noteEntities);
    }

    public void readAllNotes() {
        noteRepository.startReadAllNotes(this);
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return allNotes;
    }

    @Override
    public void onNotesRead(List<NoteEntity> noteEntities) {
        allNotes.setValue(noteEntities);
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
