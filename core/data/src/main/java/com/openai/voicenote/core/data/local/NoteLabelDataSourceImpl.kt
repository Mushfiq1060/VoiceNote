package com.openai.voicenote.core.data.local

import com.openai.voicenote.core.data.local.repository.NoteLabelRepository
import com.openai.voicenote.core.database.entities.relations.NoteLabelCrossRef
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteLabelDataSourceImpl @Inject constructor(
    private val noteLabelRepository: NoteLabelRepository
) : NoteLabelDataSource {
    override suspend fun insertNoteLabel(notesId: List<Long>, labelId: Long) {
        val crossRefList = notesId.map {
            NoteLabelCrossRef(noteId = it, labelId = labelId)
        }.toList()
        noteLabelRepository.insertNoteLabel(crossRefList)
    }

    override suspend fun deleteNoteLabelByNoteId(notesId: List<Long>) {
        noteLabelRepository.deleteNoteLabelByNoteId(notesId)
    }

    override suspend fun deleteNoteLabelByLabelId(labelsId: List<Long>) {
        noteLabelRepository.deleteNoteLabelByLabelId(labelsId)
    }

    override suspend fun removeNoteLabelRow(notesId: List<Long>, labelsId: List<Long>) {
        noteLabelRepository.removeNoteLabelRow(notesId, labelsId)
    }

    override suspend fun getAllLabelIdByNoteId(noteId: Long): List<Long> =
        noteLabelRepository.getAllLabelIdByNoteId(noteId)
}