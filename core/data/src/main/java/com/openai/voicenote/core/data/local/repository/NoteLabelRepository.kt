package com.openai.voicenote.core.data.local.repository

import com.openai.voicenote.core.database.dao.NoteLabelDao
import com.openai.voicenote.core.database.entities.relations.NoteLabelCrossRef
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteLabelRepository @Inject constructor(
    private val noteLabelDao: NoteLabelDao
)  {
    suspend fun insertNoteLabel(crossRefs: List<NoteLabelCrossRef>) {
        noteLabelDao.insertNoteLabel(crossRefs)
    }

    suspend fun deleteNoteLabelByNoteId(notesId: List<Long>) {
        noteLabelDao.deleteNoteLabelByNoteId(notesId)
    }

    suspend fun deleteNoteLabelByLabelId(labelsId: List<Long>) {
        noteLabelDao.deleteNoteLabelByLabelId(labelsId)
    }

    suspend fun removeNoteLabelRow(notesId: List<Long>, labelsId: List<Long>) {
        noteLabelDao.removeNoteLabelRow(notesId, labelsId)
    }

    suspend fun getAllLabelIdByNoteId(noteId: Long): List<Long> =
        noteLabelDao.getAllLabelIdByNoteId(noteId)

}