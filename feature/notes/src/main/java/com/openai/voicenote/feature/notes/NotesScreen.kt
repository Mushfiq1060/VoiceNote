package com.openai.voicenote.feature.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openai.voicenote.core.common.utils.Utils.toJson
import com.openai.voicenote.core.designsystem.icon.VnIcons
import com.openai.voicenote.core.designsystem.theme.VnTheme
import com.openai.voicenote.core.model.NoteResource
import com.openai.voicenote.core.model.NoteView
import com.openai.voicenote.core.ui.component.EmptyNoteList
import com.openai.voicenote.core.ui.component.FABState
import com.openai.voicenote.core.ui.component.FloatingButton
import com.openai.voicenote.core.ui.component.NoteFeedUiState
import com.openai.voicenote.core.ui.component.NoteType
import com.openai.voicenote.core.ui.component.SelectedTopAppBar
import com.openai.voicenote.core.ui.component.SelectedTopAppBarItem
import com.openai.voicenote.core.ui.component.SubFabType
import com.openai.voicenote.core.ui.component.header
import com.openai.voicenote.core.ui.component.noteFeed

enum class NotesAppBarItem {
    DRAWER,
    NOTE_VIEW
}

@Composable
fun NotesRoute(
    goToNoteEditScreen: (note: String) -> Unit,
    goToVoiceNoteScreen: () -> Unit,
    goToNoteLabelScreen: () -> Unit,
    onDrawerOpen: () -> Unit,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val feedState by viewModel.feedState.collectAsStateWithLifecycle()
    val isAnyNoteSelected by viewModel.isAnyNoteSelected.collectAsStateWithLifecycle()
    val contextMenuState by viewModel.contextMenuState.collectAsStateWithLifecycle()
    val floatingButtonState by viewModel.floatingButtonState.collectAsStateWithLifecycle()
    val noteViewState by viewModel.noteViewState.collectAsStateWithLifecycle()

    NotesScreen(
        feedState = feedState,
        fabState = floatingButtonState,
        noteViewState = noteViewState,
        isAnyNoteSelected = isAnyNoteSelected,
        isContextMenuOpen = contextMenuState,
        onNotesAppBarClick = {
            if (it == NotesAppBarItem.DRAWER) {
                onDrawerOpen()
            } else if (it == NotesAppBarItem.NOTE_VIEW) {
                viewModel.toggleNoteView()
            }
        },
        onSelectedTopAppBarClick = {
            if (it == SelectedTopAppBarItem.LABEL) {
                goToNoteLabelScreen()
            }
            viewModel.onSelectedTopAppBarClick(it)
        },
        onNoteClick = { noteResource, noteId ->
            if (isAnyNoteSelected) {
                if (noteResource.pin) {
                    viewModel.checkSelectedNote(NoteType.PINNED, noteId)
                } else {
                    viewModel.checkSelectedNote(NoteType.OTHERS, noteId)
                }
            } else {
                goToNoteEditScreen(noteResource.toJson())
            }
        },
        onNoteLongClick = { noteType, noteId ->
            viewModel.checkSelectedNote(noteType, noteId)
        },
        onSubFabClick = {
            viewModel.toggleFABState(FABState.COLLAPSED)
            if (it == SubFabType.VOICE) {
                goToVoiceNoteScreen()
            } else {
                val noteString = viewModel.getEmptyNote().toJson()
                goToNoteEditScreen(noteString)
            }
        },
        onFabStateChanged = {
            if (it == FABState.EXPANDED) {
                viewModel.toggleFABState(FABState.COLLAPSED)
            } else {
                viewModel.toggleFABState(FABState.EXPANDED)
            }
        }
    )

}

@Composable
internal fun NotesScreen(
    feedState: NoteFeedUiState,
    fabState: FABState,
    noteViewState: NoteView,
    isAnyNoteSelected: Boolean,
    isContextMenuOpen: Boolean,
    onNotesAppBarClick: (homeAppBarItem: NotesAppBarItem) -> Unit,
    onSelectedTopAppBarClick: (item: SelectedTopAppBarItem) -> Unit,
    onNoteClick: (note: NoteResource, noteId: Long) -> Unit,
    onNoteLongClick: (noteType: NoteType, noteId: Long) -> Unit,
    onSubFabClick: (type: SubFabType) -> Unit,
    onFabStateChanged: (fabState: FABState) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (!isAnyNoteSelected) {
                NotesTopAppBar(noteViewState = noteViewState, onClick = { onNotesAppBarClick(it) })
            } else {
                when (feedState) {
                    is NoteFeedUiState.Success -> {
                        SelectedTopAppBar(
                            selectedCount = feedState.selectedOtherNotes.size + feedState.selectedPinNotes.size,
                            isSelectedOtherNote = feedState.selectedOtherNotes.isNotEmpty(),
                            isContextMenuOpen = isContextMenuOpen,
                            archiveStatus = false, // future work
                            onClick = { onSelectedTopAppBarClick(it) }
                        )
                    }
                    else -> {}
                }
            }
        },
        floatingActionButton = {
            FloatingButton(
                currentState = fabState,
                onFabClicked = { onFabStateChanged(it) },
                onSubFabClicked = { onSubFabClick(it) }
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
                } else {
                    NoteList(
                        modifier = Modifier.padding(paddingValues),
                        pinnedList = feedState.pinnedNoteList,
                        otherList = feedState.otherNoteList,
                        selectedPinnedList = feedState.selectedPinNotes,
                        selectedOthersList = feedState.selectedOtherNotes,
                        noteViewState = noteViewState,
                        onClick = { note, index -> onNoteClick(note, index) },
                        onLongClick = { noteType, index -> onNoteLongClick(noteType, index) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesTopAppBar(
    noteViewState: NoteView,
    onClick: (homeAppBarItem: NotesAppBarItem) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
            .statusBarsPadding()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
    ) {
        TopAppBar(
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .shadow(elevation = 24.dp)
                .zIndex(10f)
                .height(48.dp),
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
            title = {
                Box(
                    modifier = Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Search Your Notes",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = { onClick(NotesAppBarItem.DRAWER) }
                ) {
                    Icon(
                        painter = painterResource(id = VnIcons.menu),
                        contentDescription = "menu icon",
                        modifier = Modifier.size(28.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = { onClick(NotesAppBarItem.NOTE_VIEW) }
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (noteViewState == NoteView.GRID) VnIcons.gridView else VnIcons.listView
                        ),
                        contentDescription = "view",
                        modifier = Modifier.size(28.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
        )
    }
}

@Composable
internal fun NoteList(
    modifier: Modifier = Modifier,
    pinnedList: List<NoteResource>,
    otherList: List<NoteResource>,
    selectedPinnedList: MutableSet<Long>,
    selectedOthersList: MutableSet<Long>,
    noteViewState: NoteView,
    onClick: (note: NoteResource, noteId: Long) -> Unit,
    onLongClick: (noteType: NoteType, noteId: Long) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp),
        columns = StaggeredGridCells.Fixed(
            if (noteViewState == NoteView.GRID) 1 else 2
        ),
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
            selectedList = selectedPinnedList,
            onClick = { note, noteId -> onClick(note, noteId) },
            onLongClick = { noteId -> onLongClick(NoteType.PINNED, noteId) }
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
            selectedList = selectedOthersList,
            onClick = { note, noteId -> onClick(note, noteId) },
            onLongClick = { noteId -> onLongClick(NoteType.OTHERS, noteId) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    VnTheme {
        Surface {
            NotesScreen(
                feedState = NoteFeedUiState.Success(
                    selectedPinNotes = mutableSetOf(),
                    selectedOtherNotes = mutableSetOf(),
                    pinnedNoteList = pinnedNotePreviewList,
                    otherNoteList = otherNotePreviewList
                ),
                fabState = FABState.EXPANDED,
                noteViewState = NoteView.GRID,
                isAnyNoteSelected = false,
                isContextMenuOpen = false,
                onNotesAppBarClick = {  },
                onSelectedTopAppBarClick = {  },
                onNoteClick = { _, _ -> },
                onNoteLongClick = { _, _ -> },
                onSubFabClick = {  },
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