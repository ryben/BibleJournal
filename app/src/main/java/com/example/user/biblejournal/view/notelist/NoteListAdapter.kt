package com.example.user.biblejournal.view.notelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.user.biblejournal.R
import com.example.user.biblejournal.model.database.note.NoteEntity
import com.example.user.biblejournal.utils.StringUtil

class NoteListAdapter(notes: List<NoteEntity>, private val listItemListener: ListItemListener) : RecyclerView.Adapter<NoteListAdapter.MyViewHolder>() {
    private var notes: List<NoteEntity>? = null

    init {
        setNotes(notes)
    }

    interface ListItemListener {
        fun onItemClick(noteId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return MyViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note = notes!![position]
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

        return if (null == notes) {
            0
        } else {
            notes!!.size
        }
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var textTitle: TextView
        var textTags: TextView
        var textCreationTime: TextView

        init {
            textTitle = itemView.findViewById(R.id.note_item_title)
            textTags = itemView.findViewById(R.id.note_item_tags)
            textCreationTime = itemView.findViewById(R.id.note_item_creation_time)

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            listItemListener.onItemClick(notes!![adapterPosition].id)
        }
    }
}
