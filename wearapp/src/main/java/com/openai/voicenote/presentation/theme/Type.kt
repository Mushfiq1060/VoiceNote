package com.openai.voicenote.presentation.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Typography
import com.openai.voicenote.R

private val EczarFontFamily = FontFamily(
    Font(R.font.eczar_regular),
    Font(R.font.eczar_semibold, FontWeight.SemiBold)
)
private val RobotoCondensed = FontFamily(
    Font(R.font.robotocondensed_regular),
    Font(R.font.robotocondensed_light, FontWeight.Light),
    Font(R.font.robotocondensed_bold, FontWeight.Bold)
)

val typography = Typography(
    defaultFontFamily = RobotoCondensed,
    title1 = TextStyle(
        fontFamily = EczarFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp
    ),
    title2 = TextStyle(
        fontFamily = EczarFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),
    title3 = TextStyle(
        fontFamily = EczarFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    body1 = TextStyle(
        fontFamily = EczarFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    body2 = TextStyle(
        fontFamily = EczarFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    display1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    display2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    display3 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        fontFamily = EczarFontFamily,
    ),
)