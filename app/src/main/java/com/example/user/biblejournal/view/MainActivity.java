package com.example.user.biblejournal.view;

import android.os.Bundle;
import android.widget.Toast;

import com.example.user.biblejournal.R;
import com.example.user.biblejournal.model.database.NoteEntity;
import com.example.user.biblejournal.viewmodel.NoteViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity implements EditNoteFragment.EditNoteListener, NoteListFragment.NoteListListener {
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.init(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_placeholder, new NoteListFragment())
                .commit();
    }

    @Override
    public void saveNote(NoteEntity noteEntity) {
        noteViewModel.getNoteRepository().insertNote(noteEntity);
        Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startNewNote() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder, new EditNoteFragment())
                .addToBackStack(null)
                .commit();
    }
}
