package com.openai.voicenote.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.openai.voicenote.core.database.entities.relations.LabelWithNotes
import com.openai.voicenote.core.database.entities.relations.NoteLabelCrossRef
import com.openai.voicenote.core.database.entities.relations.NoteWithLabels
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteLabelDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNoteLabel(crossRefs: List<NoteLabelCrossRef>)

    @Query("DELETE FROM note_label_cross_ref WHERE noteId IN (:notesId)")
    suspend fun deleteNoteLabelByNoteId(notesId: List<Long>)

    @Query("DELETE FROM note_label_cross_ref WHERE labelId IN (:labelsId)")
    suspend fun deleteNoteLabelByLabelId(labelsId: List<Long>)

    @Query("DELETE FROM note_label_cross_ref WHERE noteId IN (:notesId) AND labelId IN (:labelsId)")
    suspend fun removeNoteLabelRow(notesId: List<Long>, labelsId: List<Long>)

    /**
     * Do not use this. If use then need to filter out pin and other notes
     *
     * Use [observeAllPinNoteWithLabels] & [observeAllOtherNoteWithLabels]
     */
    @Transaction
    @Query("SELECT * FROM note_table")
    fun observeAllNoteWithLabels(): Flow<List<NoteWithLabels>>

    @Transaction
    @Query("SELECT * FROM note_table WHERE pin = :pin")
    fun observeAllPinNoteWithLabels(pin: Boolean = true): Flow<List<NoteWithLabels>>

    @Transaction
    @Query("SELECT * FROM note_table WHERE pin = :pin")
    fun observeAllOtherNoteWithLabels(pin: Boolean = false): Flow<List<NoteWithLabels>>

    @Transaction
    @Query("SELECT * FROM label_table")
    fun observeAllLabelWithNotes(): Flow<List<LabelWithNotes>>

    @Query("SELECT labelId FROM note_label_cross_ref WHERE noteId = :noteId")
    suspend fun getAllLabelIdByNoteId(noteId: Long): List<Long>

}