package com.openai.voicenote.core.data

import com.openai.voicenote.core.data.repository.LabelRepository
import com.openai.voicenote.core.database.model.mapToLabelResource
import com.openai.voicenote.core.database.model.mapToLabelResourceEntity
import com.openai.voicenote.core.model.LabelResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LabelLocalDataSource @Inject constructor(
    private val labelRepository: LabelRepository
) : LabelDataSource {
    override suspend fun insertLabel(labels: List<LabelResource>) {
        labelRepository.insertLabel(labels.mapToLabelResourceEntity())
    }

    override fun observeAllLabels(): Flow<List<LabelResource>> {
        return labelRepository.observeAllLabels().map {
            it.mapToLabelResource()
        }
    }

    override suspend fun updateLabel(label: LabelResource) {
        labelRepository.updateLabel(label.mapToLabelResourceEntity())
    }

    override suspend fun deleteLabels(labelsId: List<Long>) {
        labelRepository.deleteLabels(labelsId)
    }
}