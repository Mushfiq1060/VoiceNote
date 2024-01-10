package com.openai.voicenote.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.opanai.voicenote.feature.notelabel.navigation.NOTE_LABEL_ROUTE
import com.opanai.voicenote.feature.notelabel.navigation.noteLabelScreen
import com.openai.voicenote.feature.label.navigation.labelScreen
import com.openai.voicenote.feature.labeledit.navigation.LABEL_EDIT_ROUTE
import com.openai.voicenote.feature.labeledit.navigation.labelEditScreen
import com.openai.voicenote.feature.noteedit.navigation.noteEditScreen
import com.openai.voicenote.feature.notes.navigation.NOTES_ROUTE
import com.openai.voicenote.feature.notes.navigation.notesScreen
import com.openai.voicenote.feature.recordaudio.navigation.RECORD_AUDIO_ROUTE
import com.openai.voicenote.feature.recordaudio.navigation.recordAudioScreen
import com.openai.voicenote.ui.VnAppState

@Composable
fun VnNavHost(
    appState: VnAppState,
    startDestination: String = NOTES_ROUTE
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        notesScreen(
            goToNoteEditScreen = {
                appState.navigateToNoteEdit(0, it)
            },
            goToVoiceNoteScreen = { navController.navigate(RECORD_AUDIO_ROUTE) },
            goToNoteLabelScreen = { navController.navigate(NOTE_LABEL_ROUTE) },
            onDrawerOpen = { appState.openDrawer() }
        )
        noteEditScreen(
            onBackClick = { navController.popBackStack() }
        )
        labelEditScreen(
            onBackClick = { navController.popBackStack() }
        )
        labelScreen(
            onBackClick = { navController.popBackStack() },
            onDrawerOpen = { appState.openDrawer() }
        )
        recordAudioScreen(
            onBackClick = { navController.popBackStack() },
            goToNoteEditScreen = { appState.navigateToNoteEdit(1, it) }
        )
        noteLabelScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

}