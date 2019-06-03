package com.example.user.biblejournal.view.editnote

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.Spanned
import android.text.TextWatcher
import android.util.Pair
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.user.biblejournal.R
import com.example.user.biblejournal.model.database.note.NoteEntity
import com.example.user.biblejournal.viewmodel.NoteViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Objects

class EditNoteFragment : Fragment(), MyClickableSpan.ClickableSpanListener {
    private var editNoteListener: EditNoteListener? = null

    private var noteViewModel: NoteViewModel? = null
    private var editTitle: EditText? = null
    private var editContent: EditText? = null
    private var floatingActionButton: FloatingActionButton? = null
    private var bibleReaderMini: View? = null
    private var textEditedDateTime: TextView? = null


    interface EditNoteListener {
        fun backPreviousScreen()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is EditNoteListener) {
            this.editNoteListener = context
        } else {
            throw RuntimeException(context.javaClass.simpleName + " activity does not implement " + this.javaClass.simpleName)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noteViewModel = ViewModelProviders.of(Objects.requireNonNull<FragmentActivity>(activity)).get(NoteViewModel::class.java)

        val arguments = arguments
        var noteId: Int? = null
        if (null != arguments) {
            noteId = arguments.getInt(ARG_NOTE_ID_KEY)
        }

        noteViewModel!!.startNote(noteId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Request focus and show soft keyboard automatically
        return inflater.inflate(R.layout.fragment_edit_note, container, false)
    }


    override fun onClickableSpanClick() {
        textEditedDateTime!!.requestFocus()
        floatingActionButton!!.performClick()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTitle = view.findViewById(R.id.note_item_title)
        editContent = view.findViewById(R.id.note_item_content)
        textEditedDateTime = view.findViewById(R.id.text_edited_datetime)
        floatingActionButton = view.findViewById(R.id.floating_action_button)
        bibleReaderMini = view.findViewById(R.id.bible_mini_bottom_sheet)

        editContent!!.movementMethod = ClickableMovementMethod.instance
        editContent!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                noteViewModel!!.findSpannables(s, start, count)
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        val currentNote = noteViewModel!!.currentNote
        currentNote.observe(this, Observer { noteEntity ->
            editTitle!!.setText(noteEntity.title)
            editContent!!.setText(noteEntity.content)
        })

        noteViewModel!!.textSpannables.observe(this, Observer { pairs ->
            for (pair in pairs) {
                editContent!!.text.setSpan(
                        MyClickableSpan(this@EditNoteFragment),
                        pair.first,
                        pair.second,
                        Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
            }
        })

        // Setup Bottom Sheet
        val behavior = BottomSheetBehavior.from(bibleReaderMini!!)
        floatingActionButton!!.setOnClickListener {
            floatingActionButton!!.hide()
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    floatingActionButton!!.show()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })


        // Setup Bottom App Bar
        val bottomAppBar = view.findViewById<BottomAppBar>(R.id.bottom_app_bar)
        bottomAppBar.setNavigationOnClickListener {
            recordCurrentNote()
            noteViewModel!!.saveNote()
            editNoteListener!!.backPreviousScreen()
        }
        bottomAppBar.inflateMenu(R.menu.edit_note_menu)
        bottomAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_archive -> {
                    noteViewModel!!.archiveNote()
                    editNoteListener!!.backPreviousScreen()
                    Toast.makeText(activity, "Note archived", Toast.LENGTH_SHORT).show()
                }
                R.id.action_delete -> {
                    noteViewModel!!.deleteNote()
                    editNoteListener!!.backPreviousScreen()
                    Toast.makeText(activity, "Note deleted", Toast.LENGTH_SHORT).show()
                }
            }

            true
        }
    }

    private fun recordCurrentNote() {
        val currentNote = noteViewModel!!.currentNote.value
        currentNote!!.title = editTitle!!.text.toString()
        currentNote.content = editContent!!.text.toString()
    }

    override fun onPause() {
        super.onPause()
        recordCurrentNote()
        noteViewModel!!.saveNote()
    }

    override fun onStop() {
        super.onStop()
        recordCurrentNote()
        noteViewModel!!.saveNote()
    }

    companion object {

        private val ARG_NOTE_ID_KEY = "ARG_NOTE_ID"

        fun newInstance(noteId: Int?): EditNoteFragment {
            val editNoteFragment = EditNoteFragment()

            if (null != noteId) {
                val args = Bundle()
                args.putInt(EditNoteFragment.ARG_NOTE_ID_KEY, noteId)
                editNoteFragment.arguments = args
            }

            return editNoteFragment
        }
    }

}
