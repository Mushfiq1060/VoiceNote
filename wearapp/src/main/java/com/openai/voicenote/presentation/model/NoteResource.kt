package com.openai.voicenote.presentation.model

data class NoteResource(
    var noteId: Long?,
    var title: String,
    var description: String,
    var editTime: Long,
    var pin: Boolean,
    var archive: Boolean,
    var backgroundColor: Int,
    var backgroundImage: Int,
    var labelList: List<LabelResource> = listOf()
)