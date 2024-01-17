package com.openai.voicenote.core.data.local

import com.openai.voicenote.core.database.entities.relations.NoteLabelCrossRef

interface NoteLabelDataSource {
    suspend fun insertNoteLabel(notesId: List<Long>, labelId: Long)

    suspend fun deleteNoteLabelByNoteId(notesId: List<Long>)

    suspend fun deleteNoteLabelByLabelId(labelsId: List<Long>)

    suspend fun removeNoteLabelRow(notesId: List<Long>, labelsId: List<Long>)

    suspend fun getAllLabelIdByNoteId(noteId: Long): List<Long>

}