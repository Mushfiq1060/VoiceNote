package com.openai.voicenote.feature.recordaudio.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.openai.voicenote.feature.recordaudio.RecordAudioRoute

const val RECORD_AUDIO_ROUTE = "record_audio_route"

fun NavController.navigateToRecordAudio(navOptions: NavOptions) =
    navigate(RECORD_AUDIO_ROUTE, navOptions)

fun NavGraphBuilder.recordAudioScreen(
    onBackClick: () -> Unit,
    goToNoteEditScreen: (noteDescription: String) -> Unit
) {
    composable(
        route = RECORD_AUDIO_ROUTE
    ) {
        RecordAudioRoute(
            onBackClick = onBackClick,
            goToNoteEditScreen = goToNoteEditScreen
        )
    }
}