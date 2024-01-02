package com.openai.voicenote.core.data

import com.openai.voicenote.core.database.model.LabelResourceEntity
import kotlinx.coroutines.flow.Flow

interface LabelDataSource {

    suspend fun insertLabel(labels: List<LabelResourceEntity>)

    fun getAllLabels(): Flow<List<LabelResourceEntity>>

    suspend fun updateLabel(label: LabelResourceEntity)

    suspend fun deleteLabels(labelsId: List<Long>)

}