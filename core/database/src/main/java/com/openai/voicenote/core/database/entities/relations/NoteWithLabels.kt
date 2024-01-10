package com.openai.voicenote.core.database.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.openai.voicenote.core.database.entities.LabelResourceEntity
import com.openai.voicenote.core.database.entities.NoteResourceEntity
import com.openai.voicenote.core.database.entities.mapToLabelResource
import com.openai.voicenote.core.model.NoteResource

data class NoteWithLabels(
    @Embedded val notes: NoteResourceEntity,
    @Relation(
        parentColumn = "noteId",
        entityColumn = "labelId",
        associateBy = Junction(NoteLabelCrossRef::class)
    )
    val labels: List<LabelResourceEntity>
)

fun List<NoteWithLabels>.mapToNoteResource(): List<NoteResource> =
    map {
        NoteResource(
            noteId = it.notes.noteId,
            title = it.notes.title,
            description = it.notes.description,
            editTime = it.notes.editTime,
            pin = it.notes.pin,
            archive = it.notes.archive,
            backgroundColor = it.notes.backgroundColor,
            backgroundImage = it.notes.backgroundImage,
            labelList = it.labels.mapToLabelResource()
        )
    }