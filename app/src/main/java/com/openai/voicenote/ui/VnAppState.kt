package com.openai.voicenote.ui

import android.util.Log
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.openai.voicenote.feature.label.navigation.LABEL_ID
import com.openai.voicenote.feature.label.navigation.LABEL_ROUTE
import com.openai.voicenote.feature.label.navigation.navigateToLabel
import com.openai.voicenote.feature.labeledit.navigation.LABEL_EDIT_ROUTE
import com.openai.voicenote.feature.labeledit.navigation.navigateToLabelEdit
import com.openai.voicenote.feature.noteedit.navigation.NOTE_EDIT_ROUTE
import com.openai.voicenote.feature.noteedit.navigation.NOTE_TO_STRING
import com.openai.voicenote.feature.noteedit.navigation.navigateToNoteEdit
import com.openai.voicenote.feature.notes.navigation.NOTES_ROUTE
import com.openai.voicenote.feature.notes.navigation.navigateToNotes
import com.openai.voicenote.feature.recordaudio.navigation.RECORD_AUDIO_ROUTE
import com.openai.voicenote.navigation.DrawerDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Composable
fun rememberVnAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    selectedLabelId: MutableStateFlow<Long?>,
    drawerGesture: MutableStateFlow<Boolean>
): VnAppState {
    return remember {
        VnAppState(
            coroutineScope = coroutineScope,
            navController = navController,
            drawerState = drawerState,
            selectedLabelId = selectedLabelId,
            drawerGesture = drawerGesture
        )
    }
}

@Stable
class VnAppState(
    var coroutineScope: CoroutineScope,
    var navController: NavHostController,
    var drawerState: DrawerState,
    var selectedLabelId: MutableStateFlow<Long?>,
    var drawerGesture: MutableStateFlow<Boolean>
) {

    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentDrawerDestination: DrawerDestination
        @Composable get() = when (currentDestination?.route) {
            NOTES_ROUTE -> {
                DrawerDestination.NOTES
            }
            "$LABEL_ROUTE/{$LABEL_ID}" -> {
                DrawerDestination.CREATE_NEW_LABEL
            }
            else -> {
                // add all drawer destination in when condition
                // return start destination
                DrawerDestination.NOTES
            }
        }

    val drawerDestination: List<DrawerDestination> = DrawerDestination.entries

    var navListener = navController.addOnDestinationChangedListener { _, navDestination, _ ->
        val currentRoute = navDestination.route!!
        if (checkDrawerVisibility(currentRoute)) {
            drawerGesture.update { true }
            if (currentRoute != "$LABEL_ROUTE/{$LABEL_ID}") {
                selectedLabelId.update { null }
            }
        } else {
            drawerGesture.update { false }
        }
    }

    private fun checkDrawerVisibility(route: String): Boolean {
        return when (route) {
            NOTES_ROUTE -> true
            "$NOTE_EDIT_ROUTE{$NOTE_TO_STRING}" -> false
            LABEL_EDIT_ROUTE -> false
            "$LABEL_ROUTE/{$LABEL_ID}" -> true
            RECORD_AUDIO_ROUTE -> false
            else -> false
        }
    }

    fun navigateToDrawerDestination(drawerDestination: DrawerDestination, labelId: Long?) {
        val drawerNavOption = navOptions {
            popUpTo(NOTES_ROUTE) {
                inclusive = false
            }
        }
        when (drawerDestination) {
            DrawerDestination.NOTES -> navController.navigateToNotes(drawerNavOption)
            DrawerDestination.CREATE_NEW_LABEL -> {
                if (labelId == null) {
                    navController.navigateToLabelEdit(navOptions {  })
                } else {
                    selectedLabelId.update { labelId }
                    navController.navigateToLabel(drawerNavOption, labelId)
                }
            }
            DrawerDestination.ARCHIVE -> { /** go to archive screen */ }
            DrawerDestination.TRASH -> { /** go to trash screen */ }
            DrawerDestination.SETTINGS -> { /** go to settings screen */ }
            DrawerDestination.HELP_AND_FEEDBACK -> { /** go to help screen */ }
        }
    }

    /**
     * [fromWhichScreen] value is either 0 or 1.
     * If 0 then we navigate from notes screen.
     * If 1 then we navigate from voice note screen
     */
    fun navigateToNoteEdit(fromWhichScreen: Int = 0, noteString: String) {
        if (fromWhichScreen == 0) {
            navController.navigateToNoteEdit(navOptions {  }, noteString = noteString)
        } else if (fromWhichScreen == 1) {
            navController.navigateToNoteEdit(
                navOptions {
                    popUpTo(RECORD_AUDIO_ROUTE) {
                        inclusive = true
                    }
                },
                noteString = noteString
            )
        }
    }

    fun openDrawer() {
        coroutineScope.launch {
            drawerState.open()
        }
    }

}