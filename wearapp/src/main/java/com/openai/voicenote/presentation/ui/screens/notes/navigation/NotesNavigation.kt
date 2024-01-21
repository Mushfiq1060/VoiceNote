package com.openai.voicenote.presentation.ui.screens.notes.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.composable
import com.openai.voicenote.presentation.ui.screens.notes.NotesRoute

const val NOTE_FETCH_TYPE = "note_fetch_type"
const val NOTE_ROUTE = "note_route"

fun NavController.navigateToNotes(noteFetchType: String) =
    navigate("$NOTE_ROUTE/$noteFetchType")

fun NavGraphBuilder.notesScreen(
    onNavigateToNoteDetail: (String) -> Unit
) {
    composable(
        route = "${NOTE_ROUTE}/{$NOTE_FETCH_TYPE}",
        arguments = listOf(
            navArgument(NOTE_FETCH_TYPE) { type = NavType.StringType }
        )
    ) {
        NotesRoute(
            onNavigateToNoteDetail = onNavigateToNoteDetail
        )
    }
}