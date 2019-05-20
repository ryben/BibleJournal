package com.example.user.biblejournal.viewmodel;


import android.content.Context;

import com.example.user.biblejournal.model.database.NoteEntity;
import com.example.user.biblejournal.model.database.NoteRepository;
import com.example.user.biblejournal.model.note.NoteState;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class NoteViewModel extends ViewModel {
    private NoteRepository noteRepository;
    private NoteEntity currentNote;

    public void init(Context context) {
        noteRepository = new NoteRepository(context);
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


    public void saveNote(NoteEntity noteEntity) {
        if (NoteState.NEW.toString().equals(noteEntity.getState())) {
            if (!"".equals(noteEntity.getTitle()) || !"".equals(noteEntity.getContent())) {
                noteRepository.insertNote(noteEntity);
            }
        } else {
            noteRepository.updateNote(noteEntity);
        }
    }


    public void deleteNote(NoteEntity noteEntity) {
        noteRepository.updateNoteState(noteEntity, NoteState.DELETED);
        noteRepository.updateNote(noteEntity);
    }

    public void archiveNote(NoteEntity noteEntity) {
        noteRepository.updateNoteState(noteEntity, NoteState.ARCHIVED);
        noteRepository.updateNote(noteEntity);
    }
}
