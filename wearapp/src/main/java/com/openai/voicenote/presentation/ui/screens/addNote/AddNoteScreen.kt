package com.openai.voicenote.presentation.ui.screens.addNote

import android.app.RemoteInput
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.input.RemoteInputIntentHelper
import androidx.wear.input.wearableExtender
import com.openai.voicenote.core.designsystem.icon.VnIcons

@Composable
fun AddNoteRoute(
    viewModel: AddNoteViewModel = hiltViewModel()
) {
    val noteText by viewModel.noteText.collectAsStateWithLifecycle()

    AddNoteScreen(
        onNoteTextChange = viewModel::onNoteTextChange,
        noteText = noteText
    )
}

@Composable
internal fun AddNoteScreen(
    onNoteTextChange: (String) -> Unit,
    noteText: String
) {
    val scrollState = rememberScalingLazyListState()

    LaunchedEffect(Unit) {
        scrollState.animateScrollToItem(0, Int.MIN_VALUE)
    }

    Scaffold(
        timeText = { TimeText() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            InputScreen(
                onNoteTextChange = onNoteTextChange
            )
            ScalingLazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                state = scrollState
            ) {
                item {
                    Column(
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = noteText,
                            style = MaterialTheme.typography.display2
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
internal fun InputScreen(
    onNoteTextChange: (String) -> Unit
) {
    var userInput by remember { mutableStateOf("") }
    val inputTextKey = "input_text"

    val remoteInputs: List<RemoteInput> = listOf(
        RemoteInput.Builder(inputTextKey)
            .setLabel("Enter your Input")
            .wearableExtender {
                setEmojisAllowed(false)
                setInputActionType(EditorInfo.IME_ACTION_DONE)
            }.build(),
    )

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        it.data?.let { data ->
            val results: Bundle = RemoteInput.getResultsFromIntent(data)
            val newInputText: CharSequence? = results.getCharSequence(inputTextKey)
            userInput = newInputText?.toString() ?: ""
            onNoteTextChange(userInput)
        }
    }
    val intent: Intent = RemoteInputIntentHelper.createActionRemoteInputIntent()
    RemoteInputIntentHelper.putRemoteInputsExtra(intent, remoteInputs)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier.size(36.dp),
            onClick = { launcher.launch(intent) },
            enabled = userInput.isEmpty()
        ) {
            Icon(
                painter = painterResource(id = VnIcons.edit),
                contentDescription = ""
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            modifier = Modifier.size(36.dp),
            onClick = { /*TODO*/ },
            enabled = userInput.isNotEmpty()
        ) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = ""
            )
        }
    }
}