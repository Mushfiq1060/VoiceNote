package com.openai.voicenote.presentation.model

data class LabelResource(
    var labelId: Long?,
    var labelName: String,
    val noteList: List<NoteResource> = listOf()
)