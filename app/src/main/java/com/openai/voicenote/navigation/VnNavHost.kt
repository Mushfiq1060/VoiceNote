package com.openai.voicenote.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.opanai.voicenote.feature.notelabel.navigation.noteLabelScreen
import com.openai.voicenote.feature.label.navigation.labelScreen
import com.openai.voicenote.feature.labeledit.navigation.labelEditScreen
import com.openai.voicenote.feature.noteedit.navigation.noteEditScreen
import com.openai.voicenote.feature.notes.navigation.NOTES_ROUTE
import com.openai.voicenote.feature.notes.navigation.notesScreen
import com.openai.voicenote.feature.recordaudio.navigation.recordAudioScreen
import com.openai.voicenote.ui.VnAppState

@Composable
fun VnNavHost(
    appState: VnAppState,
    startDestination: String = NOTES_ROUTE
) {
    NavHost(
        navController = appState.navController,
        startDestination = startDestination
    ) {
        notesScreen(
            goToNoteEditScreen = { appState.navigateToNoteEdit(0, it) },
            goToVoiceNoteScreen = { appState.navigateToRecordAudio() },
            goToNoteLabelScreen = { appState.navigateToNoteLabel(it) },
            onDrawerOpen = { appState.openDrawer() }
        )
        noteEditScreen(
            onBackClick = { appState.popBackStack() }
        )
        labelEditScreen(
            onBackClick = { appState.popBackStack() }
        )
        labelScreen(
            onBackClick = { appState.popBackStack() },
            onDrawerOpen = { appState.openDrawer() }
        )
        recordAudioScreen(
            onBackClick = { appState.popBackStack() },
            goToNoteEditScreen = { appState.navigateToNoteEdit(1, it) }
        )
        noteLabelScreen(
            onBackClick = { appState.popBackStack() }
        )
    }

}