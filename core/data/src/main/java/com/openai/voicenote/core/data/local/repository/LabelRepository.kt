package com.openai.voicenote.core.data.local.repository

import com.openai.voicenote.core.database.dao.LabelDao
import com.openai.voicenote.core.database.dao.NoteLabelDao
import com.openai.voicenote.core.database.entities.LabelResourceEntity
import com.openai.voicenote.core.database.entities.relations.mapToLabelResource
import com.openai.voicenote.core.database.entities.relations.mapToNoteResource
import com.openai.voicenote.core.model.LabelResource
import com.openai.voicenote.core.model.NoteResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LabelRepository @Inject constructor(
    private val labelDao: LabelDao,
    private val noteLabelDao: NoteLabelDao
) {
    suspend fun insertLabel(labels: List<LabelResourceEntity>) =
        labelDao.insertLabel(labels)

    suspend fun updateLabel(label: LabelResourceEntity) {
        labelDao.updateLabel(label)
    }

    suspend fun deleteLabels(labelsId: List<Long>) {
        labelDao.deleteLabels(labelsId)
        noteLabelDao.deleteNoteLabelByLabelId(labelsId)
    }

    fun getLabelNameById(labelId: Long): Flow<String> {
        return labelDao.getLabelNameById(labelId).distinctUntilChanged()
    }

    fun observeAllLabels(): Flow<List<LabelResource>> {
        return labelDao.observeAllLabels().flatMapLatest {
            noteLabelDao.observeAllLabelWithNotes().map { labelWithNotes ->
                labelWithNotes.mapToLabelResource()
            }
        }
    }

    fun observeAllPinNotesWithLabel(labelId: Long): Flow<List<NoteResource>> {
        return labelDao.observeAllLabels().flatMapLatest {
            noteLabelDao.observeAllPinNoteWithLabels().map { noteWithLabels ->
                noteWithLabels.mapToNoteResource().filter { noteResource ->
                    noteResource.labelList.any { labelResource ->
                        labelResource.labelId == labelId
                    }
                }
            }
        }
    }

    fun observeAllOtherNotesWithLabel(labelId: Long): Flow<List<NoteResource>> {
        return labelDao.observeAllLabels().flatMapLatest {
            noteLabelDao.observeAllOtherNoteWithLabels().map { noteWithLabels ->
                noteWithLabels.mapToNoteResource().filter { noteResource ->
                    noteResource.labelList.any { labelResource ->
                        labelResource.labelId == labelId
                    }
                }
            }
        }
    }

}