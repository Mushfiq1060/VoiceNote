package com.openai.voicenote.core.database.entities.relations

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "note_label_cross_ref",
    primaryKeys = ["noteId", "labelId"]
)
data class NoteLabelCrossRef(
    @ColumnInfo(index = true)
    val noteId: Long,
    @ColumnInfo(index = true)
    val labelId: Long
)