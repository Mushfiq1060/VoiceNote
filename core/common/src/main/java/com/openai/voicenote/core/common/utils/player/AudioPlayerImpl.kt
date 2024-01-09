package com.openai.voicenote.core.common.utils.player

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioPlayerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AudioPlayer {

    private var mediaPlayer: MediaPlayer? = null

    override fun startPlayer(file: File, completionCallback: () -> Unit) {
        if (mediaPlayer == null) {
            MediaPlayer.create(context, file.toUri()).apply {
                mediaPlayer = this
                setOnCompletionListener {
                    completionCallback()
                }
                start()
            }
        } else {
            mediaPlayer?.start()
        }
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
    }

    override fun stopPlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}