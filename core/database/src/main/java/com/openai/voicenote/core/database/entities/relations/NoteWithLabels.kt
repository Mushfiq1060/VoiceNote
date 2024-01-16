package com.openai.voicenote.core.database.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.openai.voicenote.core.database.entities.LabelResourceEntity
import com.openai.voicenote.core.database.entities.NoteResourceEntity
import com.openai.voicenote.core.database.entities.mapToLabelResource
import com.openai.voicenote.core.model.NoteResource

data class NoteWithLabels(
    @Embedded val note: NoteResourceEntity,
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
            noteId = it.note.noteId,
            title = it.note.title,
            description = it.note.description,
            editTime = it.note.editTime,
            pin = it.note.pin,
            archive = it.note.archive,
            backgroundColor = it.note.backgroundColor,
            backgroundImage = it.note.backgroundImage,
            labelList = it.labels.mapToLabelResource()
        )
    }