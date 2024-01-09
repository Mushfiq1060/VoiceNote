package com.openai.voicenote.core.common.di

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import com.openai.voicenote.core.common.utils.player.AudioPlayer
import com.openai.voicenote.core.common.utils.player.AudioPlayerImpl
import com.openai.voicenote.core.common.utils.recorder.AudioRecorder
import com.openai.voicenote.core.common.utils.recorder.AudioRecorderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AudioModule {

    @Provides
    fun getMediaRecorder(@ApplicationContext context: Context): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }
    }

    @Provides
    fun getAudioRecorder(audioRecorderImpl: AudioRecorderImpl): AudioRecorder {
        return audioRecorderImpl
    }

    @Provides
    fun getAudioPlayer(audioPlayerImpl: AudioPlayerImpl): AudioPlayer {
        return audioPlayerImpl
    }

}