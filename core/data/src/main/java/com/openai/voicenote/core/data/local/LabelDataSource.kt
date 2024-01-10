package com.openai.voicenote.core.data.local

import com.openai.voicenote.core.model.LabelResource
import kotlinx.coroutines.flow.Flow

interface LabelDataSource {

    suspend fun insertLabel(labels: List<LabelResource>)

    suspend fun deleteCrossRefWithLabelsId(labelsId: List<Long>)

    fun observeAllLabels(): Flow<List<LabelResource>>

    fun observeAllLabelsWithNote(): Flow<List<LabelResource>>

    suspend fun updateLabel(label: LabelResource)

    suspend fun deleteLabels(labelsId: List<Long>)

    fun getLabelNameById(labelId: Long): Flow<String>

}