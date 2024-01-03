package com.openai.voicenote.core.model

data class NoteResource(
    val noteId: Long?,
    val title: String,
    val description: String,
    val editTime: Long,
    val pin: Boolean,
    val archive: Boolean,
    val backgroundColor: Int,
    val backgroundImage: Int
)



