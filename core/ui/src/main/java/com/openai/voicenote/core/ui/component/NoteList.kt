package com.openai.voicenote.core.ui.component

import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openai.voicenote.core.model.NoteResource
import com.openai.voicenote.core.model.NoteView
import com.openai.voicenote.core.ui.R

@Composable
fun NoteList(
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
                text = stringResource(id = R.string.core_ui_pinned),
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
                text = stringResource(id = R.string.core_ui_others),
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