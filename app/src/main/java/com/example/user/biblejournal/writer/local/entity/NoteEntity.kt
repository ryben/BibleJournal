package com.example.user.biblejournal.writer.local.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import com.example.user.biblejournal.model.note.NoteState

@Entity
data class NoteEntity(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,

        @ColumnInfo
        var title: String? = null,

        @ColumnInfo
        var content: String? = null,

        @ColumnInfo
        var dateCreated: String? = null,

        @ColumnInfo
        var dateEdited: String? = null,

        @ColumnInfo
        var tags: String? = null,

        @ColumnInfo
        var state: String? = null
) {
    companion object {

        fun newInstance(): NoteEntity {
            val newNote = NoteEntity()

            newNote.title = ""
            newNote.dateEdited = ""
            newNote.content = ""
            newNote.dateCreated = ""
            newNote.state = NoteState.NEW.toString()

            return newNote
        }
    }
}
