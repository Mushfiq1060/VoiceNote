package com.openai.voicenote.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.openai.voicenote.core.model.NoteResource
import java.io.Serializable

@Entity(tableName = "note_table")
data class NoteResourceEntity(
    @PrimaryKey(autoGenerate = true)
    var noteId: Long?,
    var title: String,
    var description: String,
    var editTime: Long,
    var pin: Boolean,
    var archive: Boolean,
    var backgroundColor: Int,
    var backgroundImage: Int
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