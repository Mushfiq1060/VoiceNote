package com.openai.voicenote.ui.screens.noteEditScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.openai.voicenote.R
import com.openai.voicenote.model.Note
import com.openai.voicenote.ui.navigation.BackPressHandler
import com.openai.voicenote.ui.navigation.NavigationItem
import com.openai.voicenote.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEdit(
    navHostController: NavHostController,
    note : Note?,
    speechToText : String?,
    noteEditViewModel: NoteEditViewModel = hiltViewModel()
) {

    val noteEditUiState by noteEditViewModel.uiState.collectAsState()

    if (speechToText != null && !noteEditViewModel.getCompose()) {
        noteEditViewModel.setCompose()
        noteEditViewModel.updateNoteText(speechToText)
    }
    else if (note != null && !noteEditViewModel.getCompose()) {
        noteEditViewModel.setCompose()
        noteEditViewModel.setCurrentNote(note)
    }

    BackPressHandler {
        navHostController.navigate(NavigationItem.Home.route) {
            popUpTo(Screen.HOME.name) {
                inclusive = true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                title = {

                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navHostController.navigate(NavigationItem.Home.route) {
                                popUpTo(Screen.HOME.name) {
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back button"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            noteEditViewModel.togglePinOfNote()
                        }
                    ) {
                        Image(
                            painter = painterResource(
                                id = checkPinStatus(noteEditUiState.currentNotePinStatus)
                            ),
                            contentDescription = "pin note",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    IconButton(
                        onClick = {
                            //Archive note
                        }
                    ) {
                        Image(
                            painter = painterResource(
                                id = checkArchiveStatus(noteEditUiState.currentNoteArchiveStatus)
                            ),
                            contentDescription = "pin note",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .scrollable(
                    orientation = Orientation.Vertical,
                    state = rememberScrollState()
                )
                .fillMaxSize()
        ) {
            TextField(
                value = noteEditViewModel.titleText,
                placeholder = {
                    Text(
                        text = "Title",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                onValueChange = { titleText ->
                    noteEditViewModel.updateTitleText(titleText)
                },
                textStyle = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
            TextField(
                value = noteEditViewModel.noteText,
                placeholder = {
                    Text(
                       text = "Note",
                       style = MaterialTheme.typography.titleMedium
                    )
                },
                onValueChange = { noteText ->
                    noteEditViewModel.updateNoteText(noteText)
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = Int.MAX_VALUE
            )
        }
    }
}

fun checkPinStatus(currentNotePin: Boolean): Int {
    if (currentNotePin) {
        return R.drawable.filled_push_pin_24
    }
    return R.drawable.outline_push_pin_24
}

fun checkArchiveStatus(currentNoteArchive : Boolean) : Int {
    if (currentNoteArchive) {
        return R.drawable.outline_unarchive_24
    }
    return R.drawable.outline_archive_24
}
