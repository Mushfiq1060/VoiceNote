package com.openai.voicenote.utils.player

import java.io.File

interface AudioPlayer {

    fun startPlayer(file : File, completionCallback : () -> Unit)

    fun pausePlayer()

    fun stopPlayer()

}