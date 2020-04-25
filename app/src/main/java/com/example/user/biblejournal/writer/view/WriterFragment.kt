package com.example.user.biblejournal.writer.view

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
import androidx.lifecycle.ViewModelProvider
import com.example.user.biblejournal.R
import com.example.user.biblejournal.model.data.VerseAddress
import com.example.user.biblejournal.writer.viewmodel.WriterViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WriterFragment : Fragment(), MyClickableSpan.ClickableSpanListener {
    private var editNoteListener: EditNoteListener? = null

    private lateinit var writerViewModel: WriterViewModel
    private lateinit var editTitle: EditText
    private lateinit var editContent: EditText
    private var floatingActionButton: FloatingActionButton? = null
    private var bibleReaderMini: View? = null
    private var verseContent: TextView? = null
    private var verseAddress: TextView? = null
    private var textEditedDateTime: TextView? = null

    companion object {

        private const val ARG_NOTE_ID_KEY = "ARG_NOTE_ID"

        fun newInstance(noteId: Int?): WriterFragment {
            val editNoteFragment = WriterFragment()

            if (null != noteId) {
                val args = Bundle()
                args.putInt(ARG_NOTE_ID_KEY, noteId)
                editNoteFragment.arguments = args
            }

            return editNoteFragment
        }
    }


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
        writerViewModel = ViewModelProvider(activity!!).get(WriterViewModel::class.java)
        writerViewModel.startNote(noteId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Request focus and show soft keyboard automatically
        return inflater.inflate(R.layout.fragment_edit_note, container, false)
    }


    override fun onClickableSpanClick(verseAddress: VerseAddress) {
        textEditedDateTime?.requestFocus()

        writerViewModel.readVerse(verseAddress)

        floatingActionButton?.performClick()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTitle = view.findViewById(R.id.note_item_title)
        editContent = view.findViewById(R.id.note_item_content)
        textEditedDateTime = view.findViewById(R.id.text_edited_datetime)
        floatingActionButton = view.findViewById(R.id.floating_action_button)
        bibleReaderMini = view.findViewById(R.id.bible_mini_bottom_sheet)
        verseContent = view.findViewById(R.id.verse_content)
        verseAddress = view.findViewById(R.id.verse_address)

        editContent.movementMethod = ClickableMovementMethod.instance
        editContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //  remove existing spans within the range
                val existingSpans = this@WriterFragment.editContent.text.getSpans(start, start + count, MyClickableSpan::class.java)
                for (span in existingSpans) {
                    editContent.text.removeSpan(span)
                }


                writerViewModel.findSpannables(s, start, count)
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        val currentNote = writerViewModel.currentNote
        currentNote.observe(viewLifecycleOwner, Observer { noteEntity ->
            editTitle.setText(noteEntity.title)
            editContent.setText(noteEntity.content)
        })

        // Set behavior when verses are detected
        writerViewModel.textSpannables.observe(viewLifecycleOwner, Observer { locatedVerseAddresses ->
            for (address in locatedVerseAddresses) {
                //  remove existing spans within the range
                val existingSpans = this@WriterFragment.editContent.text.getSpans(address.startIndex, address.endIndex, MyClickableSpan::class.java)
                for (span in existingSpans) {
                    editContent.text.removeSpan(span)
                }

                val span = MyClickableSpan(this@WriterFragment, address.verseAddress)
                editContent.text.setSpan(
                        span,
                        address.startIndex,
                        address.endIndex,
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

        writerViewModel.verseToShow.observe(viewLifecycleOwner, Observer { verseInfo ->
            verseContent?.text = verseInfo.content
            val fullVerseAddress = "${verseInfo.bookName} ${verseInfo.verseAddress.chapter}:${verseInfo.verseAddress.verse}"
            verseAddress?.text = fullVerseAddress
        })


        // Setup Bottom App Bar
        val bottomAppBar = view.findViewById<BottomAppBar>(R.id.bottom_app_bar)
        bottomAppBar.setNavigationOnClickListener {
            recordCurrentNote()
            writerViewModel.saveNote()
            editNoteListener!!.backPreviousScreen()
        }
        bottomAppBar.inflateMenu(R.menu.edit_note_menu)
        bottomAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_archive -> {
                    writerViewModel.archiveNote()
                    editNoteListener!!.backPreviousScreen()
                    Toast.makeText(activity, "Note archived", Toast.LENGTH_SHORT).show()
                }
                R.id.action_delete -> {
                    writerViewModel.deleteNote()
                    editNoteListener!!.backPreviousScreen()
                    Toast.makeText(activity, "Note deleted", Toast.LENGTH_SHORT).show()
                }
            }

            true
        }
    }

    private fun recordCurrentNote() {
        val currentNote = writerViewModel.currentNote.value
        currentNote?.title = editTitle.text.toString()
        currentNote?.content = editContent.text.toString()
    }

    override fun onPause() {
        super.onPause()
        recordCurrentNote()
        writerViewModel.saveNote()
    }

    override fun onStop() {
        super.onStop()
        recordCurrentNote()
        writerViewModel.saveNote()
    }

    override fun onDestroy() {
        super.onDestroy()
        writerViewModel.clearSpannables()
    }


}
