package com.openai.voicenote.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "label_table")
data class Label(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "LabelId")
    val labelId: Long?,
    @ColumnInfo(name = "LabelName") val labelName: String
)
