package com.openai.voicenote.core.data.repository

import com.openai.voicenote.core.database.dao.LabelResourceDao
import com.openai.voicenote.core.database.model.LabelResourceEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LabelRepository @Inject constructor(
    private val labelResourceDao: LabelResourceDao
) {

    suspend fun insertLabel(labels: List<LabelResourceEntity>) =
        labelResourceDao.insertLabel(labels)

    fun getAllLabels() = labelResourceDao.getAllLabels()

    suspend fun updateLabel(label: LabelResourceEntity) =
        labelResourceDao.updateLabel(label)

    suspend fun deleteLabels(labelsId: List<Long>) =
        labelResourceDao.deleteLabels(labelsId)

}