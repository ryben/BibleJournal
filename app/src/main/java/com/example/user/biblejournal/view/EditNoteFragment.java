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
    private static final String ARG_NOTE_ID_KEY = "ARG_NOTE_ID";

    private NoteViewModel noteViewModel;
    private EditNoteListener editNoteListener;
    private EditText editTitle;
    private EditText editContent;
    private FloatingActionButton floatingActionButton;
    private View bibleReaderMini;

    public static EditNoteFragment newInstance() {
        return new EditNoteFragment();
    }

    public static EditNoteFragment newInstance(int noteId) {
        EditNoteFragment editNoteFragment = newInstance();

        Bundle args = new Bundle();
        args.putInt(EditNoteFragment.ARG_NOTE_ID_KEY, noteId);
        editNoteFragment.setArguments(args);

        return editNoteFragment;
    }

    public interface EditNoteListener {
        void saveNote(NoteEntity noteEntity, boolean closeFragment);

        void deleteNote(NoteEntity noteEntity);

        void archiveNote(NoteEntity noteEntity);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof EditNoteListener) {
            editNoteListener = (EditNoteListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " does not implement " + editNoteListener.getClass().getName());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        noteViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(NoteViewModel.class);

        Bundle arguments = getArguments();
        if (null == arguments) {
            noteViewModel.setCurrentNote(NoteEntity.newInstance());
        } else {
            noteViewModel.setCurrentNote(noteViewModel.queryNoteById(arguments.getInt(ARG_NOTE_ID_KEY)));
        }
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

        editTitle.setText(currentNote.getTitle());
        editContent.setText(currentNote.getContent());

        floatingActionButton = view.findViewById(R.id.floating_action_button);
        bibleReaderMini = view.findViewById(R.id.bible_mini_bottom_sheet);
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bibleReaderMini);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        // Setup Bottom App Bar
        BottomAppBar bottomAppBar = view.findViewById(R.id.bottom_app_bar);
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordCurrentNote();
                editNoteListener.saveNote(noteViewModel.getCurrentNote(), true);
            }
        });
        bottomAppBar.inflateMenu(R.menu.edit_note_menu);
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_archive:
                        editNoteListener.archiveNote(noteViewModel.getCurrentNote());
                        break;
                    case R.id.action_delete:
                        editNoteListener.deleteNote(noteViewModel.getCurrentNote());
                        break;
                }
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
        editNoteListener.saveNote(noteViewModel.getCurrentNote(), false);
    }

    @Override
    public void onStop() {
        super.onStop();
        recordCurrentNote();
        editNoteListener.saveNote(noteViewModel.getCurrentNote(), false);
    }
}
