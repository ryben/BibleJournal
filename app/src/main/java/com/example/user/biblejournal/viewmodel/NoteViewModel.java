package com.example.user.biblejournal.viewmodel;


import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.user.biblejournal.model.database.note.NoteEntity;
import com.example.user.biblejournal.model.Repository;
import com.example.user.biblejournal.model.note.NoteState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NoteViewModel extends AndroidViewModel implements Repository.NotesRepositoryListener {
    private Repository repository;
    private MediatorLiveData<NoteEntity> currentNote;
    private MediatorLiveData<List<NoteEntity>> allNotes;

    public NoteViewModel(Application app) {
        super(app);
        repository = new Repository(app);

        List<NoteEntity> noteEntities = new ArrayList<>();
        allNotes = new MediatorLiveData<>();
        allNotes.setValue(noteEntities);

        currentNote = new MediatorLiveData<>();
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return allNotes;
    }

    public MediatorLiveData<NoteEntity> getCurrentNote() {
        return currentNote;
    }

    public void readAllNotes() {
        repository.executeReadAllNotes(this);
    }

    public void startNote(@Nullable Integer noteId) {
        if (null == noteId) {
            currentNote.setValue(NoteEntity.newInstance());
        } else {
            repository.executeGetNoteById(noteId, this);
        }
    }

    @Override
    public void onNotesRead(List<NoteEntity> noteEntities) {
        allNotes.setValue(noteEntities);
    }

    @Override
    public void onNoteFetchedById(NoteEntity noteEntity) {
        currentNote.setValue(noteEntity);
    }

    public void saveNote() {
        if (NoteState.NEW.toString().equals(Objects.requireNonNull(currentNote.getValue()).getState())) {
            if (!"".equals(currentNote.getValue().getTitle()) || !"".equals(currentNote.getValue().getContent())) {
                repository.insertNote(currentNote.getValue());
            }
        } else {
            repository.updateNote(currentNote.getValue());
        }
    }

    public void deleteNote() {
        repository.updateNoteState(currentNote.getValue(), NoteState.DELETED);
        repository.updateNote(currentNote.getValue());
    }

    public void archiveNote() {
        repository.updateNoteState(currentNote.getValue(), NoteState.ARCHIVED);
        repository.updateNote(currentNote.getValue());
    }


    public void findSpannables(CharSequence s, int start, int count) { // TODO: To Move to model layer
        int searchLength = 10;
        int searchStart = start - searchLength;
        int searchEnd = start + count;

        if (searchStart < 0) {
            searchStart = 0;
        }

        String searchText = s.subSequence(searchStart, searchEnd).toString();
        String toSearch = "Verse";

        if (searchText.contains(toSearch)) {
            int spanStart = searchText.indexOf(toSearch) + searchStart;
            int spanEnd = spanStart + toSearch.length();

        }

    }

}
