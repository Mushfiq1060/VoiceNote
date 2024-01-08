package com.openai.voicenote.ui

import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.openai.voicenote.core.model.LabelResource
import com.openai.voicenote.navigation.NavDrawer
import com.openai.voicenote.navigation.VnNavHost
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun VnApp(
    appState: VnAppState = rememberVnAppState(
        drawerGesture = MutableStateFlow(true),
        selectedLabelId = MutableStateFlow(null)
    ),
    labelItems: List<LabelResource>
) {

    val drawerGesture by appState.drawerGesture.collectAsState()
    val selectedLabelId by appState.selectedLabelId.collectAsState()

    Surface {
        ModalNavigationDrawer(
            drawerState = appState.drawerState,
            gesturesEnabled = drawerGesture,
            drawerContent = {
                NavDrawer(
                    destinations = appState.drawerDestination,
                    labelItems = labelItems,
                    currentDrawerDestination = appState.currentDrawerDestination,
                    selectedLabelId = selectedLabelId,
                    drawerState = appState.drawerState,
                    coroutineScope = appState.coroutineScope,
                    onNavigateToDrawerDestination = { dest, index -> appState.navigateToDrawerDestination(dest, index) }
                )
            }
        ) {
            VnNavHost(appState = appState)
        }
    }

}