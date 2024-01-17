package com.openai.voicenote.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.openai.voicenote.core.database.entities.LabelResourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LabelDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLabel(labels: List<LabelResourceEntity>): List<Long>

    @Update
    suspend fun updateLabel(label: LabelResourceEntity)

    @Query("DELETE FROM label_table WHERE labelId IN (:labelsId)")
    suspend fun deleteLabels(labelsId: List<Long>)

    @Query("SELECT labelName FROM label_table WHERE labelId = :labelId")
    fun getLabelNameById(labelId: Long): Flow<String>

    @Query("SELECT * FROM label_table")
    fun observeAllLabels(): Flow<List<LabelResourceEntity>>

}