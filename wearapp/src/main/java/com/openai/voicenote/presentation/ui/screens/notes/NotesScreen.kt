package com.openai.voicenote.presentation.ui.screens.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TitleCard
import com.openai.voicenote.presentation.model.NoteResource
import com.openai.voicenote.presentation.utils.Utils.toJson

@Composable
fun NotesRoute(
    onNavigateToNoteDetail: (String) -> Unit,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NotesScreen(
        uiState = uiState,
        onNavigateToNoteDetail = onNavigateToNoteDetail
    )
}

@Composable
internal fun NotesScreen(
    uiState: NotesUiState,
    onNavigateToNoteDetail: (String) -> Unit
) {
    Scaffold(
        timeText = { TimeText() },
        modifier = Modifier.fillMaxSize()
    ) {
        when (uiState) {
            is NotesUiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 5.dp,
                        indicatorColor = MaterialTheme.colors.onBackground
                    )
                }
            }
            is NotesUiState.Success -> {
                ScalingLazyColumn {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = uiState.notesType,
                                style = MaterialTheme.typography.display2
                            )
                        }
                    }
                    items(uiState.noteList) { noteResource ->
                        TitleCard(
                            onClick = { onNavigateToNoteDetail(noteResource.toJson()) },
                            title = {
                                Text(
                                    text = noteResource.title,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.display3
                                )
                            }
                        ) {
                            Text(
                                text = noteResource.description,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.display2
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showBackground = true, showSystemUi = true)
@Composable
fun NotesScreenPreview() {

}