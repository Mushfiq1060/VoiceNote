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

//@Entity(
//    tableName = "note_label_cross_ref",
//    foreignKeys = [
//        ForeignKey(
//            entity = NoteResourceEntity::class,
//            parentColumns = ["noteId"],
//            childColumns = ["noteId"]
//        ),
//        ForeignKey(
//            entity = LabelResourceEntity::class,
//            parentColumns = ["labelId"],
//            childColumns = ["labelId"]
//        )
//    ],
//    indices = [
//        Index(value = ["noteId"]),
//        Index(value = ["labelId"])
//    ]
//)
