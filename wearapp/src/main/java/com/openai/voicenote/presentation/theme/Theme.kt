package com.openai.voicenote.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.MaterialTheme

@Composable
fun VnWearTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = initialThemeValues.colors,
        typography = typography,
        content = content
    )
}