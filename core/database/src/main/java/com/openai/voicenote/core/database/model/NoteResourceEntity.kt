package com.openai.voicenote.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.openai.voicenote.core.model.NoteResource
import java.io.Serializable

@Entity(tableName = "note_table")
data class NoteResourceEntity(
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

fun List<NoteResourceEntity>.mapToNoteResource(): List<NoteResource> =
    map {
        NoteResource(
            noteId = it.noteId,
            title = it.title,
            description = it.description,
            editTime = it.editTime,
            pin = it.pin,
            archive = it.archive,
            backgroundColor = it.backgroundColor,
            backgroundImage = it.backgroundImage
        )
    }

fun List<NoteResource>.mapToNoteResourceEntity(): List<NoteResourceEntity> =
    map {
        NoteResourceEntity(
            noteId = it.noteId,
            title = it.title,
            description = it.description,
            editTime = it.editTime,
            pin = it.pin,
            archive = it.archive,
            backgroundColor = it.backgroundColor,
            backgroundImage = it.backgroundImage
        )
    }

fun NoteResource.mapToNoteResourceEntity(): NoteResourceEntity =
    NoteResourceEntity(
        noteId = this.noteId,
        title = this.title,
        description = this.description,
        editTime = this.editTime,
        pin = this.pin,
        archive = this.archive,
        backgroundColor = this.backgroundColor,
        backgroundImage = this.backgroundImage
    )

fun NoteResourceEntity.mapToNoteResource(): NoteResource =
    NoteResource(
        noteId = this.noteId,
        title = this.title,
        description = this.description,
        editTime = this.editTime,
        pin = this.pin,
        archive = this.archive,
        backgroundColor = this.backgroundColor,
        backgroundImage = this.backgroundImage
    )