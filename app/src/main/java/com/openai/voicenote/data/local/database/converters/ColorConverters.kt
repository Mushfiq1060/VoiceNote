package com.openai.voicenote.data.local.database.converters


import android.graphics.Color
import androidx.room.TypeConverter

class ColorConverters {

    @TypeConverter
    fun fromColor(color: Color): Int {
        return color.toArgb()
    }

    @TypeConverter
    fun toColor(color: Int): Color {
        return Color.valueOf(color)
    }

}