package com.example.user.biblejournal.view;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.user.biblejournal.R;
import com.example.user.biblejournal.model.database.NoteEntity;
import com.example.user.biblejournal.viewmodel.NoteViewModel;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class EditNoteFragment extends Fragment implements MyClickableSpan.ClickableSpanListener {
    private EditNoteListener editNoteListener;

    private static final String ARG_NOTE_ID_KEY = "ARG_NOTE_ID";

    private NoteViewModel noteViewModel;
    private EditText editTitle;
    private EditText editContent;
    private FloatingActionButton floatingActionButton;
    private View bibleReaderMini;
    private TextView textEditedDateTime;

    public static EditNoteFragment newInstance(@Nullable Integer noteId) {
        EditNoteFragment editNoteFragment = new EditNoteFragment();

        if (null != noteId) {
            Bundle args = new Bundle();
            args.putInt(EditNoteFragment.ARG_NOTE_ID_KEY, noteId);
            editNoteFragment.setArguments(args);
        }

        return editNoteFragment;
    }


    public interface EditNoteListener {
        void backPreviousScreen();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof EditNoteListener) {
            this.editNoteListener = (EditNoteListener) context;
        } else {
            throw new RuntimeException(context.getClass().getSimpleName() + " activity does not implement " + this.getClass().getSimpleName());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        noteViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(NoteViewModel.class);

        Bundle arguments = getArguments();
        Integer noteId = null;
        if (null != arguments) {
            noteId = arguments.getInt(ARG_NOTE_ID_KEY);
        }

        noteViewModel.startNote(noteId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Request focus and show soft keyboard automatically
        return inflater.inflate(R.layout.fragment_edit_note, container, false);
    }

    private void spanTextIfVerse(CharSequence s, int start, int count) { // TODO: To Move to model layer
        int searchLength = 10;
        int searchStart = start - searchLength;
        int searchEnd = start + count;

        if (searchStart < 0) {
            searchStart = 0;
        }


        String searchText = s.subSequence(searchStart, searchEnd).toString();
        String toSearch = "Verse";

        if (searchText.contains(toSearch)) {
            int spanStart = searchText.indexOf(toSearch) + searchStart;
            int spanEnd = spanStart + toSearch.length();

            editContent.getText().setSpan(new MyClickableSpan(this), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }

    }

    @Override
    public void onClickableSpanClick() {
        textEditedDateTime.requestFocus();
        floatingActionButton.performClick();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTitle = view.findViewById(R.id.note_item_title);
        editContent = view.findViewById(R.id.note_item_content);
        textEditedDateTime = view.findViewById(R.id.text_edited_datetime);
        floatingActionButton = view.findViewById(R.id.floating_action_button);
        bibleReaderMini = view.findViewById(R.id.bible_mini_bottom_sheet);

        editContent.setMovementMethod(ClickableMovementMethod.getInstance());
        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                spanTextIfVerse(s, start, count);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        final LiveData<NoteEntity> currentNote = noteViewModel.getCurrentNote();
        currentNote.observe(this, new Observer<NoteEntity>() {
            @Override
            public void onChanged(NoteEntity noteEntity) {
                editTitle.setText(noteEntity.getTitle());
                editContent.setText(noteEntity.getContent());
            }
        });

        // Setup Bottom Sheet
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bibleReaderMini);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionButton.hide();
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    floatingActionButton.show();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });


        // Setup Bottom App Bar
        BottomAppBar bottomAppBar = view.findViewById(R.id.bottom_app_bar);
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordCurrentNote();
                noteViewModel.saveNote();
                editNoteListener.backPreviousScreen();
            }
        });
        bottomAppBar.inflateMenu(R.menu.edit_note_menu);
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_archive:
                        noteViewModel.archiveNote();
                        editNoteListener.backPreviousScreen();
                        Toast.makeText(getActivity(), "Note archived", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_delete:
                        noteViewModel.deleteNote();
                        editNoteListener.backPreviousScreen();
                        Toast.makeText(getActivity(), "Note deleted", Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });
    }



    private void recordCurrentNote() {
        final NoteEntity currentNote = noteViewModel.getCurrentNote().getValue();
        currentNote.setTitle(editTitle.getText().toString());
        currentNote.setContent(editContent.getText().toString());
    }

    @Override
    public void onPause() {
        super.onPause();
        recordCurrentNote();
        noteViewModel.saveNote();
    }

    @Override
    public void onStop() {
        super.onStop();
        recordCurrentNote();
        noteViewModel.saveNote();
    }

}
