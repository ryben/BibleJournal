package com.example.user.biblejournal.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.user.biblejournal.R;
import com.example.user.biblejournal.model.database.NoteEntity;
import com.example.user.biblejournal.model.note.NoteState;
import com.example.user.biblejournal.viewmodel.NoteViewModel;

public class MainActivity extends AppCompatActivity implements EditNoteFragment.EditNoteListener, NoteListFragment.NoteListListener {
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.init(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_placeholder, NoteListFragment.newInstance())
                .commit();
    }

    @Override
    public void saveNote(NoteEntity noteEntity) {
        if (NoteState.NEW.toString().equals(noteEntity.getState())) {
            noteEntity.setState(NoteState.ACTIVE.toString());
            noteViewModel.getNoteRepository().insertNote(noteEntity);
        } else {
            noteViewModel.getNoteRepository().updateNote(noteEntity);
        }

        getSupportFragmentManager().popBackStack();
        Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteNote(NoteEntity noteEntity) {
        noteEntity.setState(NoteState.DELETED.toString());
        noteViewModel.getNoteRepository().updateNote(noteEntity);

        getSupportFragmentManager().popBackStack();
        Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void archiveNote(NoteEntity noteEntity) {
        noteEntity.setState(NoteState.ARCHIVED.toString());
        noteViewModel.getNoteRepository().updateNote(noteEntity);

        getSupportFragmentManager().popBackStack();
        Toast.makeText(this, "Note archived", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startNewNote() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder, EditNoteFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void editNote(int noteId) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder, EditNoteFragment.newInstance(noteId))
                .addToBackStack(null)
                .commit();
    }


}
