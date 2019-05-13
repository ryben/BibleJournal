package com.example.user.biblejournal.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.biblejournal.R;
import com.example.user.biblejournal.adapter.NoteListAdapter;
import com.example.user.biblejournal.model.database.NoteEntity;
import com.example.user.biblejournal.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class NoteListFragment extends Fragment implements NoteListAdapter.ListItemListener {
    private NoteListListener noteListListener;
    private FloatingActionButton floatingActionButton;
    private NoteViewModel noteViewModel;
    private RecyclerView noteListRecyclerView;

    public static NoteListFragment newInstance() {
        return new NoteListFragment();
    }

    public interface NoteListListener {
        void startNewNote();
        void editNote(int noteId);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof NoteListListener) {
            noteListListener = (NoteListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " does not implement " + noteListListener.getClass().getName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        floatingActionButton = view.findViewById(R.id.button_add_note);
        noteListRecyclerView = view.findViewById(R.id.note_list_recycler_view);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteListListener.startNewNote();
            }
        });

        noteViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(NoteViewModel.class);
        LiveData<List<NoteEntity>> notes = noteViewModel.getNotes();

        final NoteListAdapter noteListAdapter = new NoteListAdapter(notes.getValue(), this);
        noteListRecyclerView.setAdapter(noteListAdapter);
        noteListRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        notes.observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(List<NoteEntity> noteEntities) {
                noteListAdapter.setNotes(noteEntities);
            }
        });
    }


    @Override
    public void onItemClick(int noteId) {
        noteListListener.editNote(noteId);
    }


}
