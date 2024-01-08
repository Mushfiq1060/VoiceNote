package com.openai.voicenote.feature.label
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.openai.voicenote.core.designsystem.icon.VnIcons
import com.openai.voicenote.core.ui.component.EmptyNoteList

@Composable
fun LabelRoute(
    modifier: Modifier = Modifier
) {
    LabelScreen()
}

@Composable
fun LabelScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Spacer(modifier = Modifier.padding(paddingValues))
        EmptyNoteList(
            text = "No notes with this label yet",
            icon = VnIcons.label
        )
    }
}