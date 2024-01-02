package com.openai.voicenote.core.data

import com.openai.voicenote.core.data.repository.LabelRepository
import com.openai.voicenote.core.database.model.LabelResourceEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LabelLocalDataSource @Inject constructor(
    private val labelRepository: LabelRepository
) : LabelDataSource {
    override suspend fun insertLabel(labels: List<LabelResourceEntity>) {
        labelRepository.insertLabel(labels)
    }

    override fun getAllLabels(): Flow<List<LabelResourceEntity>> {
        return labelRepository.getAllLabels()
    }

    override suspend fun updateLabel(label: LabelResourceEntity) {
        labelRepository.updateLabel(label)
    }

    override suspend fun deleteLabels(labelsId: List<Long>) {
        labelRepository.deleteLabels(labelsId)
    }
}