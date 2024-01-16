package com.openai.voicenote.core.designsystem.icon

import androidx.annotation.ColorInt
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

object VnColor {
    val bgColorList = colorList.mapIndexed { index: Int, color: Int ->
        BgColor(index, color)
    }
}

data class BgColor(
    val id: Int,
    @ColorInt val colorCode: Int
)

val colorList = listOf(
    Color.Transparent.toArgb(),
    Color(0xFFFAAFA8).toArgb(),
    Color(0xFFF39F76).toArgb(),
    Color(0xFFFFF8B8).toArgb(),
    Color(0xFFE2F6D3).toArgb(),
    Color(0xFFB4DDD3).toArgb(),
    Color(0xFFD4E4ED).toArgb(),
    Color(0xFFAECCDC).toArgb(),
    Color(0xFFD3BFDB).toArgb(),
    Color(0xFFF6E2DD).toArgb(),
    Color(0xFFE9E3D4).toArgb(),
    Color(0xFFEFEFF1).toArgb(),
)