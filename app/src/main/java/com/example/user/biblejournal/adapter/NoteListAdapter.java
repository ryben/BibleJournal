package com.example.user.biblejournal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user.biblejournal.R;
import com.example.user.biblejournal.model.database.NoteEntity;
import com.example.user.biblejournal.utils.StringUtil;

import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.MyViewHolder> {
    private List<NoteEntity> notes;
    private ListItemListener listItemListener;

    public NoteListAdapter(List<NoteEntity> notes, ListItemListener listItemListener) {
        setNotes(notes);
        this.listItemListener = listItemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new MyViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NoteEntity note = notes.get(position);
        holder.textTitle.setText(note.getTitle());
        holder.textCreationTime.setText(note.getDateCreated());

        if (StringUtil.isEmpty(note.getTags())) {
            holder.textTags.setVisibility(View.GONE);
        } else {
            holder.textTags.setVisibility(View.VISIBLE);
        }
    }

    public void setNotes(List<NoteEntity> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        if (null == notes) {
            return 0;
        } else {
            return notes.size();
        }
    }

    public interface ListItemListener {
        void onItemClick(int noteId);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textTitle;
        TextView textTags;
        TextView textCreationTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.note_item_title);
            textTags = itemView.findViewById(R.id.note_item_tags);
            textCreationTime = itemView.findViewById(R.id.note_item_creation_time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listItemListener.onItemClick(notes.get(getAdapterPosition()).getId());
        }
    }
}
