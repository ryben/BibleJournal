package com.example.user.biblejournal.model.database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.user.biblejournal.model.note.NoteState;

@Entity
public class NoteEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String title;

    @ColumnInfo
    private String content;

    @ColumnInfo
    private String dateCreated;

    @ColumnInfo
    private String dateEdited;

    @ColumnInfo
    private String tags;

    @ColumnInfo
    private String state;

    public NoteEntity() {

    }

    public static NoteEntity newInstance() {
        NoteEntity newNote = new NoteEntity();

        newNote.setTitle("");
        newNote.setDateEdited("");
        newNote.setContent("");
        newNote.setDateCreated("");
        newNote.setState(NoteState.NEW.toString());

        return newNote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(String dateEdited) {
        this.dateEdited = dateEdited;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
