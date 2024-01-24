package com.openai.voicenote.core.common.utils

import android.net.Uri
import com.google.gson.Gson
import java.util.Calendar
import java.util.Date

object Utils {

    private val monthList = listOf(
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec"
    )

    fun getFormattedTime(longTime: Long): String {
        val dateObj = Date(longTime)
        val calendar = Calendar.getInstance()
        val currentCalendar = Calendar.getInstance()
        calendar.time = dateObj
        val year = calendar.get(Calendar.YEAR)
        val currentYear = currentCalendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val currentMonth = currentCalendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DATE)
        val currentDate = currentCalendar.get(Calendar.DATE)
        val hour = calendar.get(Calendar.HOUR)
        val amPm = if (calendar.get(Calendar.AM_PM) == 0) "AM" else "PM"
        val minute = calendar.get(Calendar.MINUTE)
        if (year == currentYear) {
            if (month == currentMonth && date == currentDate) {
                val hourFormat = if (hour < 10) "0$hour" else "$hour"
                val minuteFormat = if (minute < 10) "0$minute" else "$minute"
                return "$hourFormat:$minuteFormat $amPm"
            }
            return "${monthList[month]} $date"
        }
        return "${monthList[month]} $date, $year"
    }

    fun <T> T.toJson(): String {
        val gson = Gson()
        return Uri.encode(gson.toJson(this))
    }

    fun <T> String.fromJson(type: Class<T>): T {
        return Gson().fromJson(this, type)
    }

}