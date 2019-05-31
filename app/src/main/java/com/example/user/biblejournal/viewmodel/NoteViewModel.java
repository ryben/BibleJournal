package com.example.user.biblejournal.viewmodel;


import android.app.Application;
import android.util.Pair;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.user.biblejournal.model.Repository;
import com.example.user.biblejournal.model.database.note.NoteEntity;
import com.example.user.biblejournal.model.database.verse.VerseEntity;
import com.example.user.biblejournal.model.note.NoteState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NoteViewModel extends AndroidViewModel implements Repository.NotesRepositoryListener, Repository.VerseRepositoryListener {
    private Repository repository;
    private MediatorLiveData<NoteEntity> currentNote;
    private MediatorLiveData<List<NoteEntity>> allNotes;
    private MediatorLiveData<List<Pair<Integer, Integer>>> textSpannables;

    public NoteViewModel(Application app) {
        super(app);
        repository = new Repository(app);

        List<NoteEntity> noteEntities = new ArrayList<>();
        allNotes = new MediatorLiveData<>();
        allNotes.setValue(noteEntities);

        currentNote = new MediatorLiveData<>();
        textSpannables = new MediatorLiveData<>();
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return allNotes;
    }

    public MediatorLiveData<NoteEntity> getCurrentNote() {
        return currentNote;
    }

    public MediatorLiveData<List<Pair<Integer, Integer>>> getTextSpannables() {
        return textSpannables;
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
        final List<Pair<Integer, Integer>> spannables = repository.findSpannables(s, start, count);
        if (!spannables.isEmpty()) {
            textSpannables.setValue(spannables);
        }
    }

    @Override
    public void onVerseRead(VerseEntity verseEntity) {
        // TODO: Implement onVerseRead
    }
}
