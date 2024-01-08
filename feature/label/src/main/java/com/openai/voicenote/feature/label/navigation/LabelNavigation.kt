package com.openai.voicenote.feature.label.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.openai.voicenote.feature.label.LabelRoute

const val LABEL_ID = "labelId"
const val LABEL_ROUTE = "label_route"

fun NavController.navigateToLabel(navOptions: NavOptions, labelId: Long) =
    navigate("$LABEL_ROUTE/$labelId", navOptions)

fun NavGraphBuilder.labelScreen(
    onBackClick: () -> Unit,
    onDrawerOpen: () -> Unit
) {
    composable(
        route = "${LABEL_ROUTE}/{$LABEL_ID}",
        arguments = listOf(
            navArgument(LABEL_ID) { type = NavType.LongType }
        )
    ) {
        LabelRoute(
            onBackClick = onBackClick,
            onDrawerOpen = onDrawerOpen
        )
    }
}