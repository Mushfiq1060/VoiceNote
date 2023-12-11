package com.openai.voicenote.ui.screens.homeScreen

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.openai.voicenote.R
import com.openai.voicenote.model.Note
import com.openai.voicenote.ui.navigation.NavigationItem
import com.openai.voicenote.ui.theme.VoiceNoteTheme
import com.openai.voicenote.utils.ClickType
import com.openai.voicenote.utils.NoteType
import com.openai.voicenote.utils.Utils.toJson

enum class HomeAppBar {
    DRAWER_ICON,
    TOGGLE_GRID,
    CANCEL,
    CONTEXT_MENU,
    MAKE_A_COPY,
    DELETE,
    ARCHIVE,
    TOGGLE_PIN,
    DRAW,
    LABEL
}

@Composable
fun Home(
    navHostController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val homeUiState by homeViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            if (homeUiState.selectedPinNotes.size + homeUiState.selectedOtherNotes.size == 0) {
                HomeTopAppBar(
                    isGridEnable = homeUiState.isGridEnable,
                    onClick = { clickType ->
                        when (clickType) {
                            HomeAppBar.DRAWER_ICON -> {
                                // open navigation drawer
                            }
                            HomeAppBar.TOGGLE_GRID -> {
                                homeViewModel.toggleGridView()
                            }
                            else -> {
                                // do nothing
                            }
                        }
                    }
                )
            }
            else {
                SelectedTopAppBar(
                    selectedCount = homeUiState.selectedPinNotes.size + homeUiState.selectedOtherNotes.size,
                    isSelectedOtherNote = homeUiState.selectedOtherNotes.isEmpty(),
                    isContextMenuOpen = homeUiState.isContextMenuOpen,
                    onClick = { clickType ->
                        when (clickType) {
                            HomeAppBar.CANCEL -> {
                                homeViewModel.removeSelectedNotes()
                            }
                            HomeAppBar.CONTEXT_MENU -> {
                                homeViewModel.toggleContextMenuState()
                            }
                            HomeAppBar.ARCHIVE -> {
                                homeViewModel.toggleContextMenuState()
                            }
                            HomeAppBar.DELETE -> {
                                homeViewModel.toggleContextMenuState()
                                homeViewModel.deleteNotes()
                            }
                            HomeAppBar.MAKE_A_COPY -> {
                                homeViewModel.toggleContextMenuState()
                                homeViewModel.makeCopyOfNote()
                            }
                            HomeAppBar.TOGGLE_PIN -> {
                                homeViewModel.updateNotesPin()
                            }
                            HomeAppBar.LABEL -> {

                            }
                            HomeAppBar.DRAW -> {

                            }
                            else -> {
                                // do nothing
                            }
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navHostController.navigate(NavigationItem.VoiceRecord.route)
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                elevation = FloatingActionButtonDefaults.elevation()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mic_24),
                    contentDescription = "voice recording",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    ) {padding ->
        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .padding(padding)
                .padding(start = 8.dp, end = 8.dp),
            columns = StaggeredGridCells.Fixed(countColumn(homeUiState.isGridEnable)),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            flingBehavior = ScrollableDefaults.flingBehavior()
        ) {
            header {
                Text(
                    text = stringResource(id = R.string.pinned),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .paddingFromBaseline(bottom = 8.dp)
                        .padding(top = 16.dp, start = 16.dp),
                    fontWeight = FontWeight.Bold
                )
            }
            itemsIndexed(homeUiState.allPinNotes) { index, item ->
                RenderGridItem(
                    note = item,
                    isSelected = homeViewModel.checkNoteIsSelected(NoteType.PIN, index)
                ) { clickType ->
                    if (clickType == ClickType.CLICK) {
                        if (homeUiState.selectedPinNotes.isNotEmpty()
                            || homeUiState.selectedOtherNotes.isNotEmpty()
                        ) {
                            if (homeViewModel.checkNoteIsSelected(NoteType.PIN, index)) {
                                homeViewModel.removeSelectedNote(NoteType.PIN, index)
                            }
                            else {
                                homeViewModel.addSelectedNotes(NoteType.PIN, index)
                            }
                        }
                        else {
                            val noteString = item.toJson()
                            navHostController.navigate(NavigationItem.NoteEdit.route + "/$noteString" + "/clickNote" + "/1")
                        }
                    }
                    else if (clickType == ClickType.LONG_CLICK){
                        homeViewModel.addSelectedNotes(NoteType.PIN, index)
                    }
                }
            }
            header {
                Text(
                    text = stringResource(id = R.string.others),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .paddingFromBaseline(bottom = 8.dp)
                        .padding(top = 16.dp, start = 16.dp),
                    fontWeight = FontWeight.Bold
                )
            }
            itemsIndexed(homeUiState.allOtherNotes) { index, item ->
                RenderGridItem(
                    note = item,
                    isSelected = homeViewModel.checkNoteIsSelected(NoteType.Other, index)
                ) { clickType ->
                    if (clickType == ClickType.CLICK) {
                        if (homeUiState.selectedPinNotes.isNotEmpty()
                            || homeUiState.selectedOtherNotes.isNotEmpty()
                        ) {
                            if (homeViewModel.checkNoteIsSelected(NoteType.Other, index)) {
                                homeViewModel.removeSelectedNote(NoteType.Other, index)
                            }
                            else {
                                homeViewModel.addSelectedNotes(NoteType.Other, index)
                            }
                        }
                        else {
                            val noteString = item.toJson()
                            navHostController.navigate(NavigationItem.NoteEdit.route + "/$noteString" + "/clickNote" + "/1")
                        }
                    }
                    else if (clickType == ClickType.LONG_CLICK){
                        homeViewModel.addSelectedNotes(NoteType.Other, index)
                    }
                }
            }
            header {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(isGridEnable : Boolean, onClick : (type : HomeAppBar) -> Unit) {
    Box(
        modifier = Modifier.padding(16.dp)
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
                        text = stringResource(id = R.string.search_your_notes),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        onClick(HomeAppBar.DRAWER_ICON)
                    }
                ) {
                    Image(
                        painter = painterResource(
                            id = R.drawable.menu_24
                        ),
                        contentDescription = "menu icon",
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        onClick(HomeAppBar.TOGGLE_GRID)
                    }
                ) {
                    Image(
                        painter = painterResource(
                            id = checkGridStatus(isGridEnable)
                        ),
                        contentDescription = "view",
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedTopAppBar(selectedCount : Int, isSelectedOtherNote : Boolean, isContextMenuOpen : Boolean, onClick : (type : HomeAppBar) -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        title = {
            Text(
                text = "$selectedCount",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    onClick(HomeAppBar.CANCEL)
                }
            ) {
                Image(
                    painter = painterResource(
                        id = R.drawable.close_24
                    ),
                    contentDescription = "close",
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    onClick(HomeAppBar.TOGGLE_PIN)
                }
            ) {
                Image(
                    painter = painterResource(
                        id = checkPinStatus(isSelectedOtherNote)
                    ),
                    contentDescription = "pin or unpin note",
                    modifier = Modifier.size(28.dp)
                )
            }
            IconButton(
                onClick = {
                    onClick(HomeAppBar.DRAW)
                }
            ) {
                Image(
                    painter = painterResource(
                        id = R.drawable.color_palette_24
                    ),
                    contentDescription = "color palette",
                    modifier = Modifier.size(28.dp)
                )
            }
            IconButton(
                onClick = {
                    onClick(HomeAppBar.LABEL)
                }
            ) {
                Image(
                    painter = painterResource(
                        id = R.drawable.label_24
                    ),
                    contentDescription = "label note",
                    modifier = Modifier.size(28.dp)
                )
            }
            IconButton(
                onClick = {
                    onClick(HomeAppBar.CONTEXT_MENU)
                }
            ) {
                Image(
                    painter = painterResource(
                        id = R.drawable.more_vert_24
                    ),
                    contentDescription = "context menu option",
                    modifier = Modifier.size(28.dp)
                )
                DropdownMenu(
                    expanded = isContextMenuOpen,
                    onDismissRequest = {
                        Log.i("TAGG", "On Dismiss req")
                        onClick(HomeAppBar.CONTEXT_MENU)
                    }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Archive",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            onClick(HomeAppBar.ARCHIVE)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Delete",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            onClick(HomeAppBar.DELETE)
                        }
                    )
                    if (selectedCount == 1) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Make a copy",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                onClick(HomeAppBar.MAKE_A_COPY)
                            }
                        )
                    }
                }
            }
        }
    )
}

fun LazyStaggeredGridScope.header(
    content: @Composable LazyStaggeredGridItemScope.() -> Unit
) {
    item(
        span = StaggeredGridItemSpan.FullLine,
        content = content
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RenderGridItem(note : Note, isSelected : Boolean, onClick : (ClickType) -> Unit) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(getBorderWidth(isSelected), getBorderColor(isSelected)),
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .clip(shape = MaterialTheme.shapes.large)
            .shadow(
                elevation = getCardElevation(selected = isSelected),
                spotColor = MaterialTheme.colorScheme.onBackground,
            )
            .combinedClickable(
                onClick = {
                    onClick(ClickType.CLICK)
                },
                onLongClick = {
                    onClick(ClickType.LONG_CLICK)
                }
            )
    ) {
        Text(
            text = note.title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.W900,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 4.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = note.description,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 16.dp),
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )
    }
}

fun checkPinStatus(pinState : Boolean): Int {
    if (pinState) {
        return R.drawable.filled_pin_24
    }
    return R.drawable.pin_24
}

fun getCardElevation(selected: Boolean) : Dp {
    if (selected) {
        return 20.dp
    }
    return 0.dp
}

fun getBorderWidth(selected: Boolean) : Dp {
    if (!selected) {
        return 2.dp
    }
    return 4.dp
}

@Composable
fun getBorderColor(selected: Boolean): Color {
    if (!selected) {
        return MaterialTheme.colorScheme.outlineVariant
    }
    return MaterialTheme.colorScheme.onPrimaryContainer
}

fun countColumn(gridEnable: Boolean) : Int {
    if (gridEnable) {
        return 2
    }
    return 1
}

fun checkGridStatus(gridEnable: Boolean) : Int {
    if(gridEnable) {
        return R.drawable.list_view_24
    }
    return R.drawable.grid_view_24
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    VoiceNoteTheme {
        Home(navHostController = rememberNavController())
    }
}

