package com.openai.voicenote.core.common.utils.recorder

import android.media.MediaRecorder
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioRecorderImpl @Inject constructor(
    private val mediaRecorder: MediaRecorder
) : AudioRecorder {

    override fun startRecording(outputFile: File, recordingStartCallback: () -> Unit) {
        mediaRecorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(outputFile).fd)
            prepare()
            recordingStartCallback()
            start()
        }
    }

    override fun stopRecording() {
        mediaRecorder.stop()
        mediaRecorder.reset()
    }
}