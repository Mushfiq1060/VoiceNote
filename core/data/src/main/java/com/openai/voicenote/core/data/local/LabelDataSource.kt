package com.openai.voicenote.core.data.local

import com.openai.voicenote.core.model.LabelResource
import com.openai.voicenote.core.model.NoteResource
import kotlinx.coroutines.flow.Flow

interface LabelDataSource {
    suspend fun insertLabel(labels: List<LabelResource>): List<Long>

    suspend fun updateLabel(label: LabelResource)

    suspend fun deleteLabels(labelsId: List<Long>)

    fun getLabelNameById(labelId: Long): Flow<String>

    fun observeAllLabels(): Flow<List<LabelResource>>

    fun observeAllPinNotesWithLabel(labelId: Long): Flow<List<NoteResource>>

    fun observeAllOtherNotesWithLabel(labelId: Long): Flow<List<NoteResource>>

}