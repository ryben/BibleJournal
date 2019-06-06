package com.example.user.biblejournal.view.notelist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.user.biblejournal.R
import com.example.user.biblejournal.viewmodel.BibleViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Objects


class NoteListFragment : Fragment(), NoteListAdapter.ListItemListener {
    private var noteListListener: NoteListListener? = null
    private var floatingActionButton: FloatingActionButton? = null
    private var bibleViewModel: BibleViewModel? = null
    private var noteListRecyclerView: RecyclerView? = null

    interface NoteListListener {
        fun startEditNote(noteId: Int?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is NoteListListener) {
            noteListListener = context
        } else {
            throw RuntimeException(context.toString()
                    + " does not implement " + noteListListener!!.javaClass.name)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        floatingActionButton = view.findViewById(R.id.button_add_note)
        noteListRecyclerView = view.findViewById(R.id.note_list_recycler_view)

        floatingActionButton!!.setOnClickListener { noteListListener!!.startEditNote(null) }

        bibleViewModel = ViewModelProviders.of(Objects.requireNonNull<FragmentActivity>(activity)).get(BibleViewModel::class.java)
        val notes = bibleViewModel!!.allNotes

        val noteListAdapter = NoteListAdapter(notes.value!!, this)
        noteListRecyclerView!!.adapter = noteListAdapter
        noteListRecyclerView!!.layoutManager = LinearLayoutManager(view.context)

        notes.observe(this, Observer { noteEntities -> noteListAdapter.setNotes(noteEntities) })

        bibleViewModel!!.readAllNotes()
    }

    override fun onItemClick(noteId: Int) {
        noteListListener!!.startEditNote(noteId)
    }

    companion object {

        fun newInstance(): NoteListFragment {
            return NoteListFragment()
        }
    }


}
