package com.openai.voicenote.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openai.voicenote.core.database.entities.relations.NoteLabelCrossRef

@Dao
interface NoteLabelCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteLabelCrossRef(crossRefs: List<NoteLabelCrossRef>)

    @Query("DELETE FROM note_label_cross_ref WHERE noteId IN (:notesId) AND labelId = :labelId")
    suspend fun deleteCrossRefWithNotesId(notesId: List<Long>, labelId: Long)

    @Query("DELETE FROM note_label_cross_ref WHERE labelId in (:labelsId)")
    suspend fun deleteCrossRefWithLabelsId(labelsId: List<Long>)

    @Query("SELECT labelId FROM note_label_cross_ref WHERE noteId = :noteId")
    suspend fun getLabelsIdByNoteId(noteId: Long): List<Long>

    @Query("SELECT noteId FROM note_label_cross_ref WHERE labelId = :labelId")
    suspend fun getNotesByLabelId(labelId: Long): List<Long>

}