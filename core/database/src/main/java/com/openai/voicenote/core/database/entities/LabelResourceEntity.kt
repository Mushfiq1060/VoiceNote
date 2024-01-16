package com.openai.voicenote.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.openai.voicenote.core.model.LabelResource

@Entity(tableName = "label_table")
data class LabelResourceEntity(
    @PrimaryKey(autoGenerate = true)
    val labelId: Long?,
    val labelName: String
)

fun List<LabelResourceEntity>.mapToLabelResource(): List<LabelResource> =
    map { LabelResource(it.labelId, it.labelName) }

fun List<LabelResource>.mapToLabelResourceEntity(): List<LabelResourceEntity> =
    map { LabelResourceEntity(it.labelId, it.labelName) }

fun LabelResource.mapToLabelResourceEntity(): LabelResourceEntity =
    LabelResourceEntity(this.labelId, this.labelName)