package com.openai.voicenote.core.data.local.repository

import com.openai.voicenote.core.database.dao.LabelResourceDao
import com.openai.voicenote.core.database.entities.LabelResourceEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LabelRepository @Inject constructor(
    private val labelResourceDao: LabelResourceDao
) {

    suspend fun insertLabel(labels: List<LabelResourceEntity>) =
        labelResourceDao.insertLabel(labels)

    suspend fun deleteCrossRefWithLabelsId(labelsId: List<Long>) =
        labelResourceDao.deleteCrossRefWithLabelsId(labelsId)

    fun observeAllLabels() = labelResourceDao.observeAllLabels()

    fun observeAllLabelsWithNote() =
        labelResourceDao.observeAllLabelsWithNote()

    suspend fun updateLabel(label: LabelResourceEntity) =
        labelResourceDao.updateLabel(label)

    suspend fun deleteLabels(labelsId: List<Long>) =
        labelResourceDao.deleteLabels(labelsId)

    fun getLabelNameById(labelId: Long) =
        labelResourceDao.getLabelNameById(labelId)

}