package com.example.user.biblejournal.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.biblejournal.R;
import com.example.user.biblejournal.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


public class NoteListFragment extends Fragment {
    private FloatingActionButton floatingActionButton;
    private NoteViewModel noteViewModel;

    public NoteListFragment() {
        // Required empty public constructor
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
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_placeholder, new EditNoteFragment())
                        .commit();
            }
        });

        noteViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(NoteViewModel.class);

    }
}
