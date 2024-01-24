package com.openai.voicenote.core.common.di

import com.openai.voicenote.core.common.Dispatcher
import com.openai.voicenote.core.common.VnDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

@Module
@InstallIn(SingletonComponent::class)
object CoroutineScopesModule {

    @Provides
    @Singleton
    @ApplicationScope
    fun providesCoroutineScope(
        @Dispatcher(VnDispatchers.Default) dispatcher: CoroutineDispatcher
    ): CoroutineScope {
        return CoroutineScope(SupervisorJob() + dispatcher)
    }

}