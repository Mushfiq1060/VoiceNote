package com.openai.voicenote.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.openai.voicenote.core.database.dao.LabelDao
import com.openai.voicenote.core.database.dao.NoteDao
import com.openai.voicenote.core.database.dao.NoteLabelDao
import com.openai.voicenote.core.database.entities.LabelResourceEntity
import com.openai.voicenote.core.database.entities.NoteResourceEntity
import com.openai.voicenote.core.database.entities.relations.NoteLabelCrossRef

@Database(
    entities = [
        NoteResourceEntity::class,
        LabelResourceEntity::class,
        NoteLabelCrossRef::class
    ],
    version = 1,
    exportSchema = true
)
abstract class VnDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    abstract fun labelDao(): LabelDao

    abstract fun noteLabelDao(): NoteLabelDao

}