package com.openai.voicenote.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.openai.voicenote.core.database.dao.LabelResourceDao
import com.openai.voicenote.core.database.dao.NoteResourceDao
import com.openai.voicenote.core.database.entities.LabelResourceEntity
import com.openai.voicenote.core.database.entities.NoteResourceEntity

@Database(
    entities = [NoteResourceEntity::class, LabelResourceEntity::class],
    version = 1,
    exportSchema = true
)
abstract class VnDatabase : RoomDatabase() {

    abstract fun noteResourceDao(): NoteResourceDao

    abstract fun labelResourceDao(): LabelResourceDao

}