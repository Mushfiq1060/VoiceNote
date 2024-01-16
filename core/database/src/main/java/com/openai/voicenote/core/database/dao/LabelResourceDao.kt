package com.openai.voicenote.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.openai.voicenote.core.database.entities.LabelResourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LabelResourceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLabel(labels: List<LabelResourceEntity>)

    @Query("SELECT * FROM label_table")
    fun observeAllLabels(): Flow<List<LabelResourceEntity>>

    @Transaction
    @Query("SELECT * FROM label_table")
    fun observeAllLabelsWithNote(): Flow<List<LabelResourceEntity>>

    @Transaction
    @Query("DELETE FROM note_label_cross_ref WHERE labelId in (:labelsId)")
    suspend fun deleteCrossRefWithLabelsId(labelsId: List<Long>)

    @Update
    suspend fun updateLabel(label: LabelResourceEntity)

    @Query("DELETE FROM label_table WHERE labelId IN (:labelsId)")
    suspend fun deleteLabels(labelsId: List<Long>)

    @Query("SELECT LabelName FROM label_table WHERE labelId = :labelId")
    fun getLabelNameById(labelId: Long): Flow<String>

    @Transaction
    @Query("SELECT labelId FROM note_label_cross_ref WHERE noteId = :noteId")
    suspend fun getLabelsIdByNoteId(noteId: Long): List<Long>

}