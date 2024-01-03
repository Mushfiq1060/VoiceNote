package com.openai.voicenote.core.data

import com.openai.voicenote.core.database.model.LabelResourceEntity
import com.openai.voicenote.core.model.LabelResource
import kotlinx.coroutines.flow.Flow

interface LabelDataSource {

    suspend fun insertLabel(labels: List<LabelResource>)

    fun observeAllLabels(): Flow<List<LabelResource>>

    suspend fun updateLabel(label: LabelResource)

    suspend fun deleteLabels(labelsId: List<Long>)

}