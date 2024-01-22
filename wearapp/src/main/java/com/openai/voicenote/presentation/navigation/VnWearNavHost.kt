package com.openai.voicenote.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.openai.voicenote.presentation.ui.screens.addNote.navigation.addNoteScreen
import com.openai.voicenote.presentation.ui.screens.addNote.navigation.navigateToAddNote
import com.openai.voicenote.presentation.ui.screens.home.navigation.HOME_ROUTE
import com.openai.voicenote.presentation.ui.screens.home.navigation.homeScreen
import com.openai.voicenote.presentation.ui.screens.noteDetail.navigation.navigateToNoteDetail
import com.openai.voicenote.presentation.ui.screens.noteDetail.navigation.noteDetailScreen
import com.openai.voicenote.presentation.ui.screens.notes.navigation.navigateToNotes
import com.openai.voicenote.presentation.ui.screens.notes.navigation.notesScreen

@Composable
fun VnWearNavHost(
    navController: NavHostController = rememberSwipeDismissableNavController(),
    startDestination: String = HOME_ROUTE
) {
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        homeScreen(
            onNavigateToAddNote = { navController.navigateToAddNote() },
            onNavigateToViewNote = { navController.navigateToNotes(it) },
            onNavigateToHelp = {}
        )
        addNoteScreen(
            onPopBack = { navController.popBackStack() }
        )
        noteDetailScreen()
        notesScreen(
            onNavigateToNoteDetail = { navController.navigateToNoteDetail(it) }
        )
    }
}