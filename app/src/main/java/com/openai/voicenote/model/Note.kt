package com.openai.voicenote.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "NoteId")
    var noteId: Long?,
    @ColumnInfo(name = "Title") var title: String,
    @ColumnInfo(name = "Description") var description: String,
    @ColumnInfo(name = "EditTime") var editTime: Long,
    @ColumnInfo(name = "Pin") var pin: Boolean,
    @ColumnInfo(name = "Archive") var archive: Boolean,
    @ColumnInfo(name = "BackgroundColor") var backgroundColor: Int,
    @ColumnInfo(name = "BackgroundImage") var backgroundImage: Int
) : Serializable