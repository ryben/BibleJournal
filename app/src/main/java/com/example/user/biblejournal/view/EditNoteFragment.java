package com.example.user.biblejournal.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.user.biblejournal.R;
import com.example.user.biblejournal.model.database.NoteEntity;
import com.example.user.biblejournal.viewmodel.NoteViewModel;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class EditNoteFragment extends Fragment {
    private EditNoteListener editNoteListener;

    private static final String ARG_NOTE_ID_KEY = "ARG_NOTE_ID";

    private NoteViewModel noteViewModel;
    private EditText editTitle;
    private EditText editContent;
    private FloatingActionButton floatingActionButton;
    private View bibleReaderMini;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NoteEntity currentNote = noteViewModel.getCurrentNote();

        editTitle = view.findViewById(R.id.note_item_title);
        editContent = view.findViewById(R.id.note_item_content);
        floatingActionButton = view.findViewById(R.id.floating_action_button);
        bibleReaderMini = view.findViewById(R.id.bible_mini_bottom_sheet);

        editTitle.setText(currentNote.getTitle());
        editContent.setText(currentNote.getContent());

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
                        break;
                    case R.id.action_delete:
                        noteViewModel.deleteNote();
                        editNoteListener.backPreviousScreen();
                        break;
                }

                // TODO: Add Toast to notify user about deletion/archival. Make view listen to event.
                return true;
            }
        });
    }

    private void recordCurrentNote() {
        final NoteEntity currentNote = noteViewModel.getCurrentNote();
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
