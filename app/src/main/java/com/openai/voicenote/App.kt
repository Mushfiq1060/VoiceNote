package com.openai.voicenote

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.openai.voicenote.ui.navigation.graphs.RootNavGraph
import com.openai.voicenote.ui.theme.VoiceNoteTheme

@Composable
fun App() {

    VoiceNoteTheme {
        Surface {
            RootNavGraph(navController = rememberNavController())
        }
    }

}
