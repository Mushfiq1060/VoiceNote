package com.openai.voicenote.core.data.local

import com.openai.voicenote.core.data.local.repository.LabelRepository
import com.openai.voicenote.core.database.entities.mapToLabelResourceEntity
import com.openai.voicenote.core.model.LabelResource
import com.openai.voicenote.core.model.NoteResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LabelDataSourceImpl @Inject constructor(
    private val labelRepository: LabelRepository
) : LabelDataSource {
    override suspend fun insertLabel(labels: List<LabelResource>): List<Long> {
        return labelRepository.insertLabel(labels.mapToLabelResourceEntity())
    }

    override suspend fun updateLabel(label: LabelResource) {
        labelRepository.updateLabel(label.mapToLabelResourceEntity())
    }

    override suspend fun deleteLabels(labelsId: List<Long>) {
        labelRepository.deleteLabels(labelsId)
    }

    override fun getLabelNameById(labelId: Long): Flow<String> {
        return labelRepository.getLabelNameById(labelId)
    }

    override fun observeAllLabels(): Flow<List<LabelResource>> {
        return labelRepository.observeAllLabels()
    }

    override fun observeAllPinNotesWithLabel(labelId: Long): Flow<List<NoteResource>> {
        return labelRepository.observeAllPinNotesWithLabel(labelId)
    }

    override fun observeAllOtherNotesWithLabel(labelId: Long): Flow<List<NoteResource>> {
        return labelRepository.observeAllOtherNotesWithLabel(labelId)
    }

}