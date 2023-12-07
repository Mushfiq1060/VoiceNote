package com.openai.voicenote.model


import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "NoteId")
    var noteId : Int?,
    @ColumnInfo(name = "Title") var title : String,
    @ColumnInfo(name = "Description") var description : String,
    @ColumnInfo(name = "EditTime") var editTime : Long,
    @ColumnInfo(name = "Pin") var pin : Boolean,
    @ColumnInfo(name = "Archive") var archive : Boolean,
    @ColumnInfo(name = "BackgroundColor") var backgroundColor : Color,
    @ColumnInfo(name = "BackgroundImage") var backgroundImage : Int
)