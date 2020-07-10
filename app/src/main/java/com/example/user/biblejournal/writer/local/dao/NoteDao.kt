package com.example.user.biblejournal.writer.local.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.user.biblejournal.writer.local.entity.NoteEntity

@Dao
interface NoteDao {
    @get:Query("SELECT id, title, content, dateCreated, dateEdited, state, tags FROM NoteEntity WHERE state = 'ACTIVE'")
    val allNotes: List<NoteEntity>

    @Insert
    fun insertNote(noteEntity: NoteEntity)

    @Update
    fun updateNote(noteEntity: NoteEntity)

    @Query("SELECT id, title, content, dateCreated, dateEdited, state, tags from NoteEntity where id = :id")
    fun getNoteById(id: Long): NoteEntity
}
