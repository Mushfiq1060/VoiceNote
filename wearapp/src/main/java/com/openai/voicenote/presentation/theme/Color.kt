package com.openai.voicenote.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors


val Green500 = Color(0xFF04B97F)
val DarkBlue900 = Color(0xFF26282F)
val DarkBlack = Color(0xFF000000)

internal data class ThemeValues(val description: String, val colors: Colors)

internal val initialThemeValues = ThemeValues(
    "Lilac (D0BCFF)",
    Colors(
        primary = DarkBlue900,
        onPrimary = Green500,
        onSurface = Color.White,
        background = DarkBlack,
        onBackground = Color.White
    )
)