package com.openai.voicenote.utils.player

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.core.net.toUri
import java.io.File

class AudioPlayerImpl(private val context: Context) : AudioPlayer {

    var mediaPlayer: MediaPlayer? = null

    override fun startPlayer(file: File, completionCallback : () -> Unit) {
        if (mediaPlayer == null) {
            MediaPlayer.create(context, file.toUri()).apply {
                mediaPlayer = this
                setOnCompletionListener {
                    completionCallback()
                }
                start()
            }
        }
        else {
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