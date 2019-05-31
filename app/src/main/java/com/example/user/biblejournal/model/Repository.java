package com.example.user.biblejournal.model;

import android.app.Application;
import android.util.Pair;

import com.example.user.biblejournal.model.asynctasks.GetAllNotesAsyncTask;
import com.example.user.biblejournal.model.asynctasks.GetNoteByIdAsyncTask;
import com.example.user.biblejournal.model.asynctasks.GetVerseByIdAsyncTask;
import com.example.user.biblejournal.model.asynctasks.InsertNoteAsyncTask;
import com.example.user.biblejournal.model.asynctasks.UpdateNoteAsyncTask;
import com.example.user.biblejournal.model.database.AppDb;
import com.example.user.biblejournal.model.database.note.NoteDao;
import com.example.user.biblejournal.model.database.note.NoteEntity;
import com.example.user.biblejournal.model.database.verse.VerseDao;
import com.example.user.biblejournal.model.database.verse.VerseEntity;
import com.example.user.biblejournal.model.note.NoteState;

import java.util.List;

public class Repository {
    private final NoteDao noteDao;
    private final VerseDao verseDao;
    private final BibleModel bibleModel;

    public Repository(Application app) {
        AppDb db = AppDb.getInstance(app); // TODO: Use dependency injection
        noteDao = db.noteDao();
        verseDao = db.verseDao();
        bibleModel = new BibleModel();
    }

    public interface NotesRepositoryListener {
        void onNotesRead(List<NoteEntity> noteEntities);

        void onNoteFetchedById(NoteEntity noteEntity);
    }

    public interface VerseRepositoryListener {
        void onVerseRead(VerseEntity verseEntity);
    }

    public void executeReadAllNotes(NotesRepositoryListener notesRepositoryListener) { // TODO: Use RxJava
        new GetAllNotesAsyncTask(noteDao, notesRepositoryListener).execute();
    }

    public void executeGetNoteById(int id, NotesRepositoryListener notesRepositoryListener) {
        new GetNoteByIdAsyncTask(noteDao, notesRepositoryListener).execute(id);
    }

    public void updateNoteState(NoteEntity noteEntity, NoteState newState) {
        noteEntity.setState(newState.toString());
    }

    public void insertNote(NoteEntity noteEntity) {
        noteEntity.setState(NoteState.ACTIVE.toString());
        new InsertNoteAsyncTask(noteDao).execute(noteEntity);
    }

    public void updateNote(NoteEntity noteEntity) {
        new UpdateNoteAsyncTask(noteDao).execute(noteEntity);
    }

    public void executeGetVerseById(VerseRepositoryListener listener, int book, int chapter, int verse) {
        new GetVerseByIdAsyncTask(verseDao, listener).execute(book, chapter, verse);
    }

    public List<Pair<Integer, Integer>> findSpannables(CharSequence s, int start, int count) {
        return bibleModel.findSpannables(s, start, count);
    }
}
