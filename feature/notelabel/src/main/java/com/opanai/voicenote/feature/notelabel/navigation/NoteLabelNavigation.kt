package com.opanai.voicenote.feature.notelabel.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.opanai.voicenote.feature.notelabel.NoteLabelRoute

const val NOTE_LABEL_ROUTE = "note_label_route"

fun NavController.navigateToNoteLabel(
    navOptions: NavOptions
) = navigate(NOTE_LABEL_ROUTE, navOptions)

fun NavGraphBuilder.noteLabelScreen(
    onBackClick: () -> Unit
) {
    composable(route = NOTE_LABEL_ROUTE) {
        NoteLabelRoute(onBackClick = onBackClick)
    }
}