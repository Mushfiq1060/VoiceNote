package com.openai.voicenote.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.openai.voicenote.core.database.model.LabelResourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LabelResourceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLabel(labels: List<LabelResourceEntity>)

    @Query("SELECT * FROM label_table")
    fun getAllLabels(): Flow<List<LabelResourceEntity>>

    @Update
    suspend fun updateLabel(label: LabelResourceEntity)

    @Query("DELETE FROM label_table WHERE LabelId IN (:labelsId)")
    suspend fun deleteLabels(labelsId: List<Long>)

}