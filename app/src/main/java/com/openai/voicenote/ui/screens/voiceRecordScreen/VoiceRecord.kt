package com.openai.voicenote.ui.screens.voiceRecordScreen

import android.app.Activity
import android.util.Log
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.openai.voicenote.R
import com.openai.voicenote.ui.navigation.NavigationItem

@Composable
fun VoiceRecord(
    navHostController: NavHostController,
    voiceRecordViewModel: VoiceRecordViewModel = hiltViewModel()
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
            Column(
                modifier = Modifier
                    .weight(0.83f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (voiceRecordUiState.isRecordStarted) {
                    Text(
                        text = "${voiceRecordUiState.timerMinutes}:${voiceRecordUiState.timerSeconds}.${voiceRecordUiState.timerMilliseconds}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    VoiceRecordAnimation(
                        voiceRecordUiState.isRecordStopped,
                        voiceRecordUiState.isPlayPaused
                    )
                }
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
                        voiceRecordUiState.isRecordStopped
                    ) {
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
                            id = checkRecordStatus(
                                voiceRecordUiState.isRecordStarted,
                                voiceRecordUiState.isRecordStopped
                            )
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
                    if (voiceRecordUiState.isRecordStopped) {
                        if (!voiceRecordUiState.isSpeechToTextConvertStart) {
                            Image(
                                painter = painterResource(
                                    id = R.drawable.speech_to_text_24
                                ),
                                contentDescription = "speech to text",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clickable(
                                        interactionSource = MutableInteractionSource(),
                                        indication = null,
                                        onClick = {
                                            voiceRecordViewModel.convertSpeechToText { response ->
                                                if (response.success == true) {
                                                    navHostController.navigate(NavigationItem.NoteEdit.route + "/noNote" + "/${response.text}" + "/2")
                                                } else {
                                                    Toast
                                                        .makeText(
                                                            context,
                                                            response.text,
                                                            Toast.LENGTH_LONG
                                                        )
                                                        .show()
                                                }
                                            }
                                        }
                                    )
                            )
                        } else {
                            ShowLoaderAnimation()
                        }
                    } else {
                        Spacer(modifier = Modifier.size(48.dp))
                    }
                }
            }

        }
    }
}

@Composable
fun VoiceRecordAnimation(recordStopped: Boolean, playPaused: Boolean) {
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
fun ShowLoaderAnimation() {
    val rawComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loader))
    val progress by animateLottieCompositionAsState(
        composition = rawComposition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        modifier = Modifier.size(108.dp),
        composition = rawComposition,
        progress = { progress },
    )
}

fun checkRecordStatus(recordStarted: Boolean, recordStopped: Boolean): Int {
    if (!recordStarted || recordStopped) {
        return R.drawable.record_24
    }
    return R.drawable.pause_24
}

fun checkPlayStatus(playPaused: Boolean): Int {
    if (playPaused) {
        return R.drawable.play_24
    }
    return R.drawable.pause_24
}

@Preview(showBackground = true)
@Composable
fun VoiceRecordPreview() {
    VoiceRecord(navHostController = rememberNavController())
}