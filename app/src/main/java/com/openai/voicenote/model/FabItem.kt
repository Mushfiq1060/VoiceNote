package com.openai.voicenote.model

import androidx.compose.ui.graphics.painter.Painter
import com.openai.voicenote.ui.component.SubFabType

data class FabItem(
    val icon : Painter,
    val label : String,
    val type : SubFabType
)