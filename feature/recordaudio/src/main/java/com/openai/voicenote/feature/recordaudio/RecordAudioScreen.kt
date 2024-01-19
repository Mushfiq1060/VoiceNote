package com.openai.voicenote.feature.recordaudio

import android.widget.Toast
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.openai.voicenote.core.common.utils.Utils.toJson
import com.openai.voicenote.core.designsystem.icon.VnIcons
import com.openai.voicenote.core.designsystem.theme.VnTheme
import com.openai.voicenote.core.ui.component.CircularLoader
import com.openai.voicenote.core.ui.component.SimpleTopAppBar

@Composable
fun RecordAudioRoute(
    onBackClick: () -> Unit,
    goToNoteEditScreen: (noteDescription: String) -> Unit,
    viewModel: RecordAudioViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val recordAudioUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val timerState by viewModel.timerState.collectAsStateWithLifecycle()


    RecordAudioScreen(
        onBackClick = { onBackClick() },
        recordAudioUiState = recordAudioUiState,
        timerState = timerState,
        onClickPlay = { viewModel.startPlaying() },
        onClickRecord = { viewModel.startRecording() },
        onClickSpeechToText = {
            viewModel.convertSpeechToText { response, note ->
                if (response.success == true) {
                    goToNoteEditScreen(note.toJson())
                } else {
                    Toast.makeText(context, response.text, Toast.LENGTH_LONG).show()
                }
            }
        }
    )
}

@Composable
fun RecordAudioScreen(
    onBackClick: () -> Unit,
    recordAudioUiState: RecordAudioUiState,
    timerState: String,
    onClickPlay: () -> Unit,
    onClickRecord: () -> Unit,
    onClickSpeechToText: () -> Unit
) {
    Scaffold(
        topBar = {
            SimpleTopAppBar(
                navigationIcon = VnIcons.arrowBack,
                title = "Record Audio",
                onClickNavigationIcon = { onBackClick() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(0.83f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (recordAudioUiState.isRecordStarted) {
                    Text(
                        text = timerState,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    VoiceRecordAnimation(
                        recordStopped = recordAudioUiState.isRecordStopped,
                        playPaused = recordAudioUiState.isPlayPaused
                    )
                }
            }
            Box(
                modifier = Modifier
                    .weight(0.17f)
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
            ) {
                AudioButtonSection(
                    recordAudioUiState = recordAudioUiState,
                    onClickPlay = { onClickPlay() },
                    onClickRecord = { onClickRecord() },
                    onClickSpeechToText = { onClickSpeechToText() }
                )
            }
        }
    }
}

@Composable
fun VoiceRecordAnimation(
    recordStopped: Boolean,
    playPaused: Boolean
) {
    val rawComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.voice_record_anim))
    val progress by animateLottieCompositionAsState(
        composition = rawComposition,
        isPlaying = (!recordStopped || !playPaused),
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = rawComposition,
        progress = { progress }
    )
}

@Composable
fun AudioButtonSection(
    recordAudioUiState: RecordAudioUiState,
    onClickPlay: () -> Unit,
    onClickRecord: () -> Unit,
    onClickSpeechToText: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (recordAudioUiState.isRecordStarted &&
            recordAudioUiState.isRecordStopped
        ) {
            Image(
                painter = painterResource(
                    id = checkPlayStatus(recordAudioUiState.isPlayPaused)
                ),
                contentDescription = "play button",
                modifier = Modifier
                    .size(48.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = { onClickPlay() }
                    )
            )
        } else {
            Spacer(modifier = Modifier.size(48.dp))
        }
        Image(
            painter = painterResource(
                id = checkRecordStatus(
                    recordAudioUiState.isRecordStarted,
                    recordAudioUiState.isRecordStopped
                )
            ),
            contentDescription = "record button",
            modifier = Modifier
                .size(64.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = { onClickRecord() }
                )
        )
        if (recordAudioUiState.isRecordStopped) {
            if (!recordAudioUiState.isSpeechToTextConvertStart) {
                Image(
                    painter = painterResource(
                        id = VnIcons.speechToText
                    ),
                    contentDescription = "speech to text",
                    modifier = Modifier
                        .size(48.dp)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null,
                            onClick = { onClickSpeechToText() }
                        )
                )
            } else {
                CircularLoader(
                    color = MaterialTheme.colorScheme.onBackground,
                    strokeWidth = 5.dp
                )
            }
        } else {
            Spacer(modifier = Modifier.size(48.dp))
        }
    }
}

fun checkRecordStatus(recordStarted: Boolean, recordStopped: Boolean): Int {
    if (!recordStarted || recordStopped) {
        return VnIcons.record
    }
    return VnIcons.pause
}

fun checkPlayStatus(playPaused: Boolean): Int {
    if (playPaused) {
        return VnIcons.play
    }
    return VnIcons.pause
}

@Preview(showBackground = true)
@Composable
fun RecordAudioScreenPreview() {
    VnTheme {
        Surface {
            RecordAudioScreen(
                onBackClick = { },
                recordAudioUiState = RecordAudioUiState(),
                timerState = "00:00.00",
                onClickPlay = {  },
                onClickRecord = {  },
                onClickSpeechToText = {  }
            )
        }
    }
}