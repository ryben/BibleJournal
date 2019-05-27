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
import java.util.Objects;

public class NoteViewModel extends AndroidViewModel implements NoteRepository.NotesRepositoryListener {
    private NoteRepository noteRepository;
    private MediatorLiveData<NoteEntity> currentNote;
    private MediatorLiveData<List<NoteEntity>> allNotes;

    public NoteViewModel(Application app) {
        super(app);
        noteRepository = new NoteRepository(app);

        List<NoteEntity> noteEntities = new ArrayList<>();
        allNotes = new MediatorLiveData<>();
        allNotes.setValue(noteEntities);

        currentNote = new MediatorLiveData<>();
    }

    public void readAllNotes() {
        noteRepository.executeReadAllNotes(this);
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return allNotes;
    }

    @Override
    public void onNotesRead(List<NoteEntity> noteEntities) {
        allNotes.setValue(noteEntities);
    }

    @Override
    public void onNoteFetchedById(NoteEntity noteEntity) {
        currentNote.setValue(noteEntity);
    }

    public MediatorLiveData<NoteEntity> getCurrentNote() {
        return currentNote;
    }

    public void startNote(@Nullable Integer noteId) {
        if (null == noteId) {
            currentNote.setValue(NoteEntity.newInstance());
        } else {
            noteRepository.executeGetNoteById(noteId, this);
        }
    }

    public void saveNote() {
        if (NoteState.NEW.toString().equals(Objects.requireNonNull(currentNote.getValue()).getState())) {
            if (!"".equals(currentNote.getValue().getTitle()) || !"".equals(currentNote.getValue().getContent())) {
                noteRepository.insertNote(currentNote.getValue());
            }
        } else {
            noteRepository.updateNote(currentNote.getValue());
        }
    }

    public void deleteNote() {
        noteRepository.updateNoteState(currentNote.getValue(), NoteState.DELETED);
        noteRepository.updateNote(currentNote.getValue());
    }

    public void archiveNote() {
        noteRepository.updateNoteState(currentNote.getValue(), NoteState.ARCHIVED);
        noteRepository.updateNote(currentNote.getValue());
    }

}
