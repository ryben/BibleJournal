package com.example.user.biblejournal.notelist.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.user.biblejournal.R
import com.example.user.biblejournal.core.ui.BaseFragment
import com.example.user.biblejournal.writer.viewmodel.WriterViewModel
import com.example.user.biblejournal.notelist.view.adapter.NoteListAdapter
import kotlinx.android.synthetic.main.fragment_note_list.*


class NoteListFragment : BaseFragment(), NoteListAdapter.NoteListItemClickListener {
    private lateinit var writerViewModel: WriterViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        writerViewModel = ViewModelProvider(requireActivity()).get(WriterViewModel::class.java)

        val notes = writerViewModel.allNotes

        val noteListAdapter = NoteListAdapter(this, notes.value!!)
        rvNoteList.adapter = noteListAdapter
        rvNoteList.layoutManager = LinearLayoutManager(view.context)

        notes.observe(viewLifecycleOwner, Observer { noteEntities -> noteListAdapter.setNotes(noteEntities) })

        writerViewModel.readAllNotes()

        btnAddNote.setOnClickListener {
            navigate(R.id.action_noteListFragment_to_writerFragment)
        }
    }

    override fun onNoteListItemClick(noteId: Long) {
        navigate(NoteListFragmentDirections.actionNoteListFragmentToWriterFragment(noteId))
    }
}