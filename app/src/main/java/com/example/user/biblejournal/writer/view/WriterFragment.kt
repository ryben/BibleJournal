package com.example.user.biblejournal.writer.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.Spanned
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.user.biblejournal.R
import com.example.user.biblejournal.core.util.Utils
import com.example.user.biblejournal.reader.repository.VerseAddress
import com.example.user.biblejournal.writer.viewmodel.WriterViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_edit_note.*

class WriterFragment : Fragment(), MyClickableSpan.ClickableSpanListener {
    private var editNoteListener: EditNoteListener? = null

    private lateinit var writerViewModel: WriterViewModel

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

        val noteId = WriterFragmentArgs.fromBundle(requireArguments()).noteId

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
        fab?.performClick()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editItemContent.movementMethod = ClickableMovementMethod.instance
        editItemContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //  remove existing spans within the range
                val existingSpans = this@WriterFragment.editItemContent.text.getSpans(start, start + count, MyClickableSpan::class.java)
                for (span in existingSpans) {
                    editItemContent.text.removeSpan(span)
                }
                writerViewModel.findSpannables(s, start, count)
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        val currentNote = writerViewModel.currentNote
        currentNote.observe(viewLifecycleOwner, Observer { noteEntity ->
            editItemTitle.setText(noteEntity.title)
            editItemContent.setText(noteEntity.content)
        })

        // Set behavior when verses are detected
        writerViewModel.textSpannables.observe(viewLifecycleOwner, Observer { locatedVerseAddresses ->
            for (address in locatedVerseAddresses) {
                //  remove existing spans within the range
                val existingSpans = this@WriterFragment.editItemContent.text.getSpans(address.startIndex, address.endIndex, MyClickableSpan::class.java)
                for (span in existingSpans) {
                    editItemContent.text.removeSpan(span)
                }

                val span = MyClickableSpan(this@WriterFragment, address.verseAddress)
                editItemContent.text.setSpan(
                        span,
                        address.startIndex,
                        address.endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        })

        // Setup Bottom Sheet
        val behavior = BottomSheetBehavior.from(bibleMiniBottomSheet!!)
        fab!!.setOnClickListener {
            fab!!.hide()
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    fab!!.show()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        writerViewModel.verseToShow.observe(viewLifecycleOwner, Observer { verseInfo ->
            textVerseContent?.text = verseInfo.content
            val fullVerseAddress = "${verseInfo.bookName} ${verseInfo.verseAddress.chapter}:${verseInfo.verseAddress.verse}"
            textVerseAddress?.text = fullVerseAddress
        })


        // Setup Bottom App Bar
        val bottomAppBar = view.findViewById<BottomAppBar>(R.id.bottom_app_bar)
        bottomAppBar.setNavigationOnClickListener {
            recordCurrentNote()
            Utils.hideKeyboard(activity)
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
        currentNote?.title = editItemTitle.text.toString()
        currentNote?.content = editItemContent.text.toString()
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
