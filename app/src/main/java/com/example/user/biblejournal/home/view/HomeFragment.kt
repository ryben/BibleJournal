package com.example.user.biblejournal.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.user.biblejournal.R
import com.example.user.biblejournal.core.ui.BaseFragment
import com.example.user.biblejournal.editnote.viewmodel.BibleViewModel
import com.example.user.biblejournal.home.view.adapter.NoteListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeFragment : BaseFragment(), NoteListAdapter.NoteListItemClickListener {

    private var floatingActionButton: FloatingActionButton? = null
    private var bibleViewModel: BibleViewModel? = null
    private var noteListRecyclerView: RecyclerView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        floatingActionButton = view.findViewById(R.id.button_add_note)
        noteListRecyclerView = view.findViewById(R.id.note_list_recycler_view)

        bibleViewModel = ViewModelProviders.of(activity!!).get(BibleViewModel::class.java)
        val notes = bibleViewModel!!.allNotes

        val noteListAdapter = NoteListAdapter(this, notes.value!!)
        noteListRecyclerView!!.adapter = noteListAdapter
        noteListRecyclerView!!.layoutManager = LinearLayoutManager(view.context)

        notes.observe(this, Observer { noteEntities -> noteListAdapter.setNotes(noteEntities) })

        bibleViewModel!!.readAllNotes()
    }

    override fun onNoteListItemClick() {
        navigate(R.id.action_noteListFragment_to_editNoteFragment)
    }

}
