package com.openai.voicenote.core.model

data class LabelResource(
    var labelId: Long?,
    var labelName: String,
    val noteList: List<NoteResource> = listOf()
)
