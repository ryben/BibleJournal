package com.example.user.biblejournal.home.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.user.biblejournal.R
import com.example.user.biblejournal.writer.local.entity.NoteEntity
import com.example.user.biblejournal.core.util.StringUtil

class NoteListAdapter(itemClickListener : NoteListItemClickListener, notes: List<NoteEntity>) : RecyclerView.Adapter<NoteListAdapter.MyViewHolder>() {
    private lateinit var notes: List<NoteEntity>
    private lateinit var itemClickListener: NoteListItemClickListener

    init {
        setNotes(notes)
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return MyViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note = notes[position]
        holder.noteId = note.id
        holder.textTitle.text = note.title
        holder.textCreationTime.text = note.dateCreated

        if (StringUtil.isEmpty(note.tags)) {
            holder.textTags.visibility = View.GONE
        } else {
            holder.textTags.visibility = View.VISIBLE
        }
    }

    fun setNotes(notes: List<NoteEntity>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var noteId: Long? = null

        override fun onClick(p0: View?) {
            if (noteId != null) {
                itemClickListener.onNoteListItemClick(noteId!!)
            }
        }

        var textTitle: TextView = itemView.findViewById(R.id.editItemTitle)
        var textTags: TextView = itemView.findViewById(R.id.note_item_tags)
        var textCreationTime: TextView = itemView.findViewById(R.id.note_item_creation_time)

        init {
            itemView.setOnClickListener(this)
        }
    }

    interface NoteListItemClickListener {
        fun onNoteListItemClick(noteId : Long)
    }
}
