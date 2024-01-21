package com.openai.voicenote.presentation.ui.screens.noteDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.openai.voicenote.presentation.model.NoteResource

@Composable
fun NoteDetailRoute(
    viewModel: NoteDetailViewModel = hiltViewModel()
) {
    NoteDetailScreen(
        note = viewModel.note
    )
}

@Composable
internal fun NoteDetailScreen(
    note: NoteResource
) {
    val scrollState = rememberScalingLazyListState()

    LaunchedEffect(Unit) {
        scrollState.animateScrollToItem(0, Int.MIN_VALUE)
    }

    Scaffold(
        timeText = { TimeText() },
        modifier = Modifier.fillMaxSize()
    ) {
        ScalingLazyColumn(
            state = scrollState
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.size(32.dp))
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.display3,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = note.description,
                        style = MaterialTheme.typography.display2
                    )
                    Spacer(modifier = Modifier.size(32.dp))
                }
            }
        }
    }
}