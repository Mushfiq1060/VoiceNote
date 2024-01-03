package com.openai.voicenote.feature.home

import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openai.voicenote.core.designsystem.icon.VnIcons
import com.openai.voicenote.core.designsystem.theme.VnTheme
import com.openai.voicenote.core.model.NoteResource
import com.openai.voicenote.core.ui.component.EmptyNoteList
import com.openai.voicenote.core.ui.component.FABState
import com.openai.voicenote.core.ui.component.FloatingButton
import com.openai.voicenote.core.ui.component.NoteFeedUiState
import com.openai.voicenote.core.ui.component.NoteType
import com.openai.voicenote.core.ui.component.header
import com.openai.voicenote.core.ui.component.noteFeed

@Composable
internal fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val feedState by viewModel.feedState.collectAsStateWithLifecycle()
    val isAnyNoteSelected by viewModel.isAnyNoteSelected.collectAsStateWithLifecycle()
    val floatingButtonState by viewModel.floatingButtonState.collectAsStateWithLifecycle()

    HomeScreen(
        feedState = feedState,
        fabState = floatingButtonState,
        onClick = { noteResource, index ->
            if (isAnyNoteSelected) {
                if (noteResource.pin) {
                    viewModel.removeSelectedNote(NoteType.PINNED, index)
                } else {
                    viewModel.removeSelectedNote(NoteType.OTHERS, index)
                }
            } else {
                // navigate to note edit screen with noteResource
            }
        },
        onLongClick = { noteType, index ->
            viewModel.addSelectedNote(noteType, index)
        },
        onFabStateChanged = { viewModel.changeFABState(it) }
    )

}

@Composable
internal fun HomeScreen(
    feedState: NoteFeedUiState,
    fabState: FABState,
    onClick: (note: NoteResource, index: Int) -> Unit,
    onLongClick: (noteType: NoteType, index: Int) -> Unit,
    onFabStateChanged: (fabState: FABState) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingButton(
                currentState = fabState,
                onFabClicked = { onFabStateChanged(it) },
                onSubFabClicked = {

                }
            )
        }
    ) { paddingValues ->
        when (feedState) {
            is NoteFeedUiState.Loading -> {
                // show loader in here
            }
            is NoteFeedUiState.Success -> {
                if (feedState.otherNoteList.isEmpty() && feedState.pinnedNoteList.isEmpty()) {
                    EmptyNoteList(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        text = "Notes you add appear here",
                        icon = VnIcons.note
                    )
                }
                else {
                    NoteList(
                        pinnedList = feedState.pinnedNoteList,
                        otherList = feedState.otherNoteList,
                        onClick = { note, index -> onClick(note, index) },
                        onLongClick = { noteType, index ->  onLongClick(noteType, index) }
                    )
                }
            }
        }
    }
}

@Composable
internal fun NoteList(
    pinnedList: List<NoteResource>,
    otherList: List<NoteResource>,
    onClick: (note: NoteResource, index: Int) -> Unit,
    onLongClick: (noteType: NoteType, index: Int) -> Unit
) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp),
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        flingBehavior = ScrollableDefaults.flingBehavior()
    ) {
        header {
            Text(
                text = "Pinned",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .paddingFromBaseline(bottom = 8.dp)
                    .padding(top = 16.dp, start = 16.dp),
                fontWeight = FontWeight.Bold
            )
        }
        noteFeed(
            noteItems = pinnedList,
            isSelected = false,
            onClick = { note, index -> onClick(note, index) },
            onLongClick = { index -> onLongClick(NoteType.PINNED, index) }
        )
        header {
            Text(
                text = "Others",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .paddingFromBaseline(bottom = 8.dp)
                    .padding(top = 16.dp, start = 16.dp),
                fontWeight = FontWeight.Bold
            )
        }
        noteFeed(
            noteItems = otherList,
            isSelected = false,
            onClick = { note, index -> onClick(note, index) },
            onLongClick = { index ->onLongClick(NoteType.OTHERS, index) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    VnTheme {
        Surface {
            HomeScreen(
                feedState = NoteFeedUiState.Success(
                    selectedPinNotes = mutableSetOf(),
                    selectedOtherNotes = mutableSetOf(),
                    pinnedNoteList = pinnedNotePreviewList,
                    otherNoteList = otherNotePreviewList
                ),
                fabState = FABState.EXPANDED,
                onClick = { _, _ -> },
                onLongClick = { _, _ -> },
                onFabStateChanged = { }
            )
        }
    }
}

val pinnedNotePreviewList = listOf<NoteResource>(
    NoteResource(
        noteId = 1,
        title = "One",
        description = "Something is wrong.\nWhy you are not help me?\nOne day I will rise again.\nAndroid developer.",
        editTime = 263566L,
        pin = true,
        archive = false,
        backgroundColor = Color.Transparent.toArgb(),
        backgroundImage = 3
    ),
    NoteResource(
        noteId = 2,
        title = "One",
        description = "Something is wrong.\nWhy you are not help me?\nOne day I will rise again.",
        editTime = 263566L,
        pin = true,
        archive = false,
        backgroundColor = Color.Transparent.toArgb(),
        backgroundImage = 2
    ),
)

val otherNotePreviewList = listOf<NoteResource>(
    NoteResource(
        noteId = 3,
        title = "One",
        description = "Something is wrong.\nWhy you are not help me?\nOne day I will rise again.\nAndroid developer.",
        editTime = 263566L,
        pin = true,
        archive = false,
        backgroundColor = Color.Transparent.toArgb(),
        backgroundImage = 6
    ),
    NoteResource(
        noteId = 4,
        title = "One",
        description = "Something is wrong.\nWhy you are not help me?\nOne day I will rise again.",
        editTime = 263566L,
        pin = true,
        archive = false,
        backgroundColor = Color.Transparent.toArgb(),
        backgroundImage = 9
    ),
)