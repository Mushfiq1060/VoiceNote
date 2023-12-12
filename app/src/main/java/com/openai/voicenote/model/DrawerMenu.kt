package com.openai.voicenote.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.openai.voicenote.DrawerMenuOption

data class DrawerMenu(
    @StringRes var title: Int,
    @DrawableRes var icon: Int,
    var menuOption: DrawerMenuOption
)