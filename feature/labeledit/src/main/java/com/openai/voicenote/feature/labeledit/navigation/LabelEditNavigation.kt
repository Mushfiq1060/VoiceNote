package com.openai.voicenote.feature.labeledit.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.openai.voicenote.feature.labeledit.LabelEditRoute

const val LABEL_EDIT_ROUTE = "label_edit_route"

fun NavController.navigateToLabelEdit(
    navOptions: NavOptions
) = navigate(LABEL_EDIT_ROUTE, navOptions)

fun NavGraphBuilder.labelEditScreen(
    onBackClick: () -> Unit
) {
    composable(route = LABEL_EDIT_ROUTE) {
        LabelEditRoute(onBackClick = onBackClick)
    }
}