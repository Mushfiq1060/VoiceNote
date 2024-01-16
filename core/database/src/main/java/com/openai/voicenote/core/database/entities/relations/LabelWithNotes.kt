package com.openai.voicenote.core.database.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.openai.voicenote.core.database.entities.LabelResourceEntity
import com.openai.voicenote.core.database.entities.NoteResourceEntity
import com.openai.voicenote.core.database.entities.mapToNoteResource
import com.openai.voicenote.core.model.LabelResource

data class LabelWithNotes(
    @Embedded val label: LabelResourceEntity,
    @Relation(
        parentColumn = "labelId",
        entityColumn = "noteId",
        associateBy = Junction(NoteLabelCrossRef::class)
    )
    val notes: List<NoteResourceEntity>
)

fun List<LabelWithNotes>.mapToLabelResource(): List<LabelResource> =
    map {
        LabelResource(
            labelId = it.label.labelId,
            labelName = it.label.labelName,
            noteList = it.notes.mapToNoteResource()
        )
    }