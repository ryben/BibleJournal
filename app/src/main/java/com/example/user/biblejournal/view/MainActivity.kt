package com.example.user.biblejournal.view

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import com.example.user.biblejournal.R
import com.example.user.biblejournal.view.editnote.EditNoteFragment
import com.example.user.biblejournal.view.notelist.NoteListFragment

class MainActivity : AppCompatActivity(), EditNoteFragment.EditNoteListener, NoteListFragment.NoteListListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (null == savedInstanceState) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_placeholder, NoteListFragment.newInstance())
                    .commit()
        }
    }

    override fun backPreviousScreen() {
        // TODO: Use Jetpack Navigation component
        supportFragmentManager.popBackStack()
    }

    override fun startEditNote(noteId: Int?) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_placeholder, EditNoteFragment.newInstance(noteId))
                .addToBackStack(null)
                .commit()
    }


}
