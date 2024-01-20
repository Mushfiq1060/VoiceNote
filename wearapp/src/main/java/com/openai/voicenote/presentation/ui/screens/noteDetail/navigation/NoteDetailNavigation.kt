package com.openai.voicenote.presentation.ui.screens.noteDetail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.composable
import com.openai.voicenote.presentation.ui.screens.noteDetail.NoteDetailRoute

const val NOTE_FETCH_TYPE = "note_fetch_type"
const val NOTE_DETAIL_ROUTE = "note_detail_route"

fun NavController.navigateToNoteDetail(noteFetchType: String) =
    navigate("$NOTE_DETAIL_ROUTE/$noteFetchType")

fun NavGraphBuilder.noteDetailScreen() {
    composable(
        route = "${NOTE_DETAIL_ROUTE}/{$NOTE_FETCH_TYPE}",
        arguments = listOf(
            navArgument(NOTE_FETCH_TYPE) { type = NavType.StringType }
        )
    ) {
        NoteDetailRoute()
    }
}