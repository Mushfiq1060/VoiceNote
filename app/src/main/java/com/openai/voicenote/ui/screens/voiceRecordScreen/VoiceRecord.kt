package com.openai.voicenote.ui.screens.voiceRecordScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.openai.voicenote.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceRecord(
    navHostController: NavHostController,
    voiceRecordViewModel: VoiceRecordViewModel = viewModel()
) {
    val context = LocalContext.current
    val voiceRecordUiState by voiceRecordViewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(0.83f)
            ) {

            }
            Box(
                modifier = Modifier
                    .weight(0.17f)
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (voiceRecordUiState.isRecordStarted &&
                            voiceRecordUiState.isRecordStopped) {
                        Image(
                            painter = painterResource(
                                id = checkPlayStatus(voiceRecordUiState.isPlayPaused)
                            ),
                            contentDescription = "play button",
                            modifier = Modifier
                                .size(48.dp)
                                .clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null,
                                    onClick = {
                                        voiceRecordViewModel.startPlaying(context)
                                    }
                                )
                        )
                    } else {
                        Spacer(modifier = Modifier.size(48.dp))
                    }
                    Image(
                        painter = painterResource(
                            id = checkRecordStatus(voiceRecordUiState.isRecordStarted, voiceRecordUiState.isRecordStopped)
                        ),
                        contentDescription = "record button",
                        modifier = Modifier
                            .size(64.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null,
                                onClick = {
                                    voiceRecordViewModel.startRecording(context)
                                }
                            )
                    )
                    Image(
                        painter = painterResource(
                            id = R.drawable.speech_to_text
                        ),
                        contentDescription = "speech to text",
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

        }
    }
}

fun checkRecordStatus(recordStarted: Boolean, recordStopped: Boolean): Int {
    if (!recordStarted || recordStopped) {
        return R.drawable.record
    }
    return R.drawable.pause
}

fun checkPlayStatus(playPaused: Boolean): Int {
    if (playPaused) {
        return R.drawable.play
    }
    return R.drawable.pause
}

@Preview(showBackground = true)
@Composable
fun VoiceRecordPreview() {
    VoiceRecord(navHostController = rememberNavController())
}