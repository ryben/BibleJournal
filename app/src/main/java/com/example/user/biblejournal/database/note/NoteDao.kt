package com.example.user.biblejournal.database.note


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {
    @get:Query("SELECT id, title, content, dateCreated, dateEdited, state, tags FROM NoteEntity WHERE state = 'ACTIVE'")
    val allNotes: List<NoteEntity>

    @Insert
    fun insertNote(noteEntity: NoteEntity)

    @Update
    fun updateNote(noteEntity: NoteEntity)

    @Query("SELECT id, title, content, dateCreated, dateEdited, state, tags from NoteEntity where id = :id")
    fun getNoteById(id: Int): NoteEntity
}
