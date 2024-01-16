package com.opanai.voicenote.feature.notelabel.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.opanai.voicenote.feature.notelabel.NoteLabelRoute

const val NOTES_ID_LIST = "notes_id_list"
const val NOTE_LABEL_ROUTE = "note_label_route"

fun NavController.navigateToNoteLabel(
    navOptions: NavOptions,
    noteIdList: String
) = navigate("$NOTE_LABEL_ROUTE/$noteIdList", navOptions)

fun NavGraphBuilder.noteLabelScreen(
    onBackClick: () -> Unit
) {
    composable(
        route = "${NOTE_LABEL_ROUTE}/{$NOTES_ID_LIST}",
        arguments = listOf(
            navArgument(NOTES_ID_LIST) { type = NavType.StringType }
        )
    ) {
        NoteLabelRoute(onBackClick = onBackClick)
    }
}