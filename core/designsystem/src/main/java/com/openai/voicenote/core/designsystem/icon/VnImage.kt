package com.openai.voicenote.core.designsystem.icon

import androidx.annotation.DrawableRes
import com.openai.voicenote.core.designsystem.R

object VnImage {
    val bgImageList = listOf<BgImage>(
        BgImage(0, R.drawable.no_image),
        BgImage(1, R.drawable.one),
        BgImage(2, R.drawable.two),
        BgImage(3, R.drawable.three),
        BgImage(4, R.drawable.four),
        BgImage(5, R.drawable.five),
        BgImage(6, R.drawable.six),
        BgImage(7, R.drawable.seven),
        BgImage(8, R.drawable.eight),
        BgImage(9, R.drawable.nine)
    )
}

data class BgImage(
    val id: Int,
    @DrawableRes val drawableId: Int
)