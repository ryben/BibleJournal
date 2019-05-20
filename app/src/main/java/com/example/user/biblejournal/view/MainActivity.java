package com.example.user.biblejournal.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.user.biblejournal.R;
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
    public void goBack() {
        // TODO: Use Jetpack Navigation component
        getSupportFragmentManager().popBackStack();
    }

    public void startNewNote() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder, EditNoteFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    public void editNote(int noteId) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder, EditNoteFragment.newInstance(noteId))
                .addToBackStack(null)
                .commit();
    }


}
