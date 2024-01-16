package com.openai.voicenote.core.common.utils

import java.util.Timer
import kotlin.concurrent.schedule

class QueryDeBouncer<T>(
    private val durationInMilliseconds: Long,
    val onValue: (T) -> Unit
) {

    private var timer: Timer? = null

    var typeTValue: T? = null
        set(value) {
            field = value
            timer?.cancel()
            timer = Timer()
            timer!!.schedule(durationInMilliseconds) {
                if (value != null) {
                    onValue(value)
                }
            }
        }

}