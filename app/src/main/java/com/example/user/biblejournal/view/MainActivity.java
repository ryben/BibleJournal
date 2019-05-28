package com.example.user.biblejournal.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.user.biblejournal.R;
import com.example.user.biblejournal.view.editnote.EditNoteFragment;
import com.example.user.biblejournal.view.notelist.NoteListFragment;

public class MainActivity extends AppCompatActivity implements EditNoteFragment.EditNoteListener, NoteListFragment.NoteListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_placeholder, NoteListFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void backPreviousScreen() {
        // TODO: Use Jetpack Navigation component
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void startEditNote(Integer noteId) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder, EditNoteFragment.newInstance(noteId))
                .addToBackStack(null)
                .commit();
    }


}
