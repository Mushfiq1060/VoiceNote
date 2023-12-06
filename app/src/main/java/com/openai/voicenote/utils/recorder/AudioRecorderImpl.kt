package com.openai.voicenote.utils.recorder

import android.content.Context
import android.media.MediaRecorder
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioRecorderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mediaRecorder: MediaRecorder
) : AudioRecorder {

    override fun startRecording(outputFile: File) {
        mediaRecorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(outputFile).fd)
            prepare()
            start()
        }
    }

    override fun stopRecording() {
        mediaRecorder.stop()
        mediaRecorder.reset()
    }
}