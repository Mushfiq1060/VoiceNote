package com.openai.voicenote.presentation.ui.screens.noteDetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.composable
import com.openai.voicenote.presentation.ui.screens.noteDetail.NoteDetailRoute

const val NOTE_STRING = "note_string"
const val NOTE_DETAIL_ROUTE = "note_detail_route"

fun NavController.navigateToNoteDetail(noteFetchType: String) =
    navigate("$NOTE_DETAIL_ROUTE/$noteFetchType")

fun NavGraphBuilder.noteDetailScreen() {
    composable(
        route = "${NOTE_DETAIL_ROUTE}/{$NOTE_STRING}",
        arguments = listOf(
            navArgument(NOTE_STRING) { type = NavType.StringType }
        )
    ) {
        NoteDetailRoute()
    }
}