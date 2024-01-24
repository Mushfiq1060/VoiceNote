package com.openai.voicenote.core.database.di

import com.openai.voicenote.core.database.VnDatabase
import com.openai.voicenote.core.database.dao.LabelDao
import com.openai.voicenote.core.database.dao.NoteDao
import com.openai.voicenote.core.database.dao.NoteLabelDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DaoModule {
    @Provides
    fun providesNoteDao(vnDatabase: VnDatabase): NoteDao {
        return vnDatabase.noteDao()
    }

    @Provides
    fun providesLabelDao(vnDatabase: VnDatabase): LabelDao {
        return vnDatabase.labelDao()
    }

    @Provides
    fun providesNoteLabelDao(vnDatabase: VnDatabase): NoteLabelDao {
        return vnDatabase.noteLabelDao()
    }

}