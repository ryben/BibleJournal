package com.example.user.biblejournal.view.editnote

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.Spanned
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.user.biblejournal.R
import com.example.user.biblejournal.viewmodel.BibleViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EditNoteFragment : Fragment(), MyClickableSpan.ClickableSpanListener {
    private var editNoteListener: EditNoteListener? = null

    private var bibleViewModel: BibleViewModel? = null
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

        val arguments = arguments
        val noteId: Int? = arguments?.getInt(ARG_NOTE_ID_KEY)

        bibleViewModel = activity?.let { ViewModelProviders.of(it).get(BibleViewModel::class.java) }
        bibleViewModel!!.startNote(noteId)
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
                //  remove existing spans within the range
                val spans = editContent!!.text.getSpans(start, start + count, MyClickableSpan::class.java)
                for (span in spans) {
                    editContent!!.text.removeSpan(span)
                }

                bibleViewModel!!.findSpannables(s, start, count)
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        val currentNote = bibleViewModel!!.currentNote
        currentNote.observe(this, Observer { noteEntity ->
            editTitle!!.setText(noteEntity.title)
            editContent!!.setText(noteEntity.content)
        })

        bibleViewModel!!.textSpannables.observe(this, Observer { pairs ->
            for (pair in pairs) {
                editContent!!.text.setSpan(
                        MyClickableSpan(this@EditNoteFragment),
                        pair.first,
                        pair.second,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
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
            bibleViewModel!!.saveNote()
            editNoteListener!!.backPreviousScreen()
        }
        bottomAppBar.inflateMenu(R.menu.edit_note_menu)
        bottomAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_archive -> {
                    bibleViewModel!!.archiveNote()
                    editNoteListener!!.backPreviousScreen()
                    Toast.makeText(activity, "Note archived", Toast.LENGTH_SHORT).show()
                }
                R.id.action_delete -> {
                    bibleViewModel!!.deleteNote()
                    editNoteListener!!.backPreviousScreen()
                    Toast.makeText(activity, "Note deleted", Toast.LENGTH_SHORT).show()
                }
            }

            true
        }
    }

    private fun recordCurrentNote() {
        val currentNote = bibleViewModel!!.currentNote.value
        currentNote!!.title = editTitle!!.text.toString()
        currentNote.content = editContent!!.text.toString()
    }

    override fun onPause() {
        super.onPause()
        recordCurrentNote()
        bibleViewModel!!.saveNote()
    }

    override fun onStop() {
        super.onStop()
        recordCurrentNote()
        bibleViewModel!!.saveNote()
    }

    override fun onDestroy() {
        super.onDestroy()
        bibleViewModel?.clearSpannables()
    }

    companion object {

        private const val ARG_NOTE_ID_KEY = "ARG_NOTE_ID"

        fun newInstance(noteId: Int?): EditNoteFragment {
            val editNoteFragment = EditNoteFragment()

            if (null != noteId) {
                val args = Bundle()
                args.putInt(ARG_NOTE_ID_KEY, noteId)
                editNoteFragment.arguments = args
            }

            return editNoteFragment
        }
    }

}
