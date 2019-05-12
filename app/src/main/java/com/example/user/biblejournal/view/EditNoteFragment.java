package com.example.user.biblejournal.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.user.biblejournal.R;
import com.example.user.biblejournal.model.database.NoteEntity;
import com.example.user.biblejournal.model.note.NoteFactory;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EditNoteFragment extends Fragment {
    public interface EditNoteListener {
        void saveNote(NoteEntity noteEntity);
    }

    private EditNoteListener editNoteListener;
    private EditText editTitle;
    private EditText editContent;


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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        editTitle = view.findViewById(R.id.note_item_title);
        editContent = view.findViewById(R.id.note_item_content);

        view.findViewById(R.id.bottom_app_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteEntity newNote = NoteFactory.createNewNote(editTitle.getText().toString(), editContent.getText().toString());
                editNoteListener.saveNote(newNote);
            }
        });
    }
}
