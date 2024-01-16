package com.openai.voicenote.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.openai.voicenote.core.database.entities.NoteResourceEntity
import com.openai.voicenote.core.database.entities.relations.NoteLabelCrossRef
import com.openai.voicenote.core.database.entities.relations.NoteWithLabels
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteResourceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(notes: List<NoteResourceEntity>): List<Long>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteLabelCrossRef(crossRefs: List<NoteLabelCrossRef>)

    @Query("DELETE FROM note_label_cross_ref WHERE noteId IN (:notesId) AND labelId = :labelId")
    suspend fun deleteCrossRefWithNotesId(notesId: List<Long>, labelId: Long)

    @Query("SELECT * FROM note_table")
    fun observeAllNotes(): Flow<List<NoteResourceEntity>>

    @Transaction
    @Query("SELECT * FROM note_table")
    fun observeAllNoteWithLabels(): Flow<List<NoteWithLabels>>

    @Query("SELECT * FROM note_table WHERE pin = :pin")
    fun observeAllPinNotes(pin: Boolean = true): Flow<List<NoteResourceEntity>>

    @Transaction
    @Query("SELECT * FROM note_table WHERE pin = :pin")
    fun observeAllPinNotesWithLabels(pin: Boolean = true): Flow<List<NoteWithLabels>>

    @Query("SELECT * FROM note_table WHERE pin = :pin")
    fun observeAllOtherNotes(pin: Boolean = false): Flow<List<NoteResourceEntity>>

    @Transaction
    @Query("SELECT * FROM note_table WHERE pin = :pin")
    fun observeAllOtherNotesWithLabels(pin: Boolean = false): Flow<List<NoteWithLabels>>

    @Query("SELECT * FROM note_table WHERE noteId = :noteId")
    suspend fun getNoteById(noteId: Long): NoteResourceEntity

    @Update
    suspend fun updateNote(note: NoteResourceEntity)

    @Query("UPDATE note_table SET pin = :pin WHERE noteId IN (:notesId)")
    suspend fun togglePinStatus(notesId: List<Long>, pin: Boolean)

    @Query("UPDATE note_table SET archive = :archive WHERE noteId IN (:notesId)")
    suspend fun toggleArchiveStatus(notesId: List<Long>, archive: Boolean)

    @Query("DELETE FROM note_table WHERE noteId In (:notesId)")
    suspend fun deleteNotes(notesId: List<Long>)

    @Transaction
    @Query("SELECT noteId FROM note_label_cross_ref WHERE labelId = :labelId")
    suspend fun getNotesByLabelId(labelId: Long): List<Long>

}