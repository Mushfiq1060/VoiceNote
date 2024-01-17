package com.openai.voicenote.core.data.di

import com.openai.voicenote.core.data.local.LabelDataSource
import com.openai.voicenote.core.data.local.LabelDataSourceImpl
import com.openai.voicenote.core.data.local.NoteDataSource
import com.openai.voicenote.core.data.local.NoteDataSourceImpl
import com.openai.voicenote.core.data.local.NoteLabelDataSource
import com.openai.voicenote.core.data.local.NoteLabelDataSourceImpl
import com.openai.voicenote.core.data.local.repository.UserDataRepository
import com.openai.voicenote.core.data.local.repository.UserDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsNoteDataSource(
        noteDataSourceImpl: NoteDataSourceImpl
    ): NoteDataSource

    @Binds
    internal abstract fun bindsLabelDataSource(
        labelDataSourceImpl: LabelDataSourceImpl
    ): LabelDataSource

    @Binds
    internal abstract fun bindsNoteLabelDataSource(
        noteLabelDataSourceImpl: NoteLabelDataSourceImpl
    ): NoteLabelDataSource

    @Binds
    internal abstract fun bindsUserDataRepository(
        userDataRepositoryImpl: UserDataRepositoryImpl
    ): UserDataRepository

}