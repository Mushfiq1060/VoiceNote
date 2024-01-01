package com.openai.voicenote.core.database.di

import com.openai.voicenote.core.database.VnDatabase
import com.openai.voicenote.core.database.dao.LabelResourceDao
import com.openai.voicenote.core.database.dao.NoteResourceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DaoModule {

    @Provides
    fun providesNoteResourceDao(vnDatabase: VnDatabase): NoteResourceDao {
        return vnDatabase.noteResourceDao()
    }

    fun providesLabelResourceDao(vnDatabase: VnDatabase): LabelResourceDao {
        return vnDatabase.labelResourceDao()
    }

}