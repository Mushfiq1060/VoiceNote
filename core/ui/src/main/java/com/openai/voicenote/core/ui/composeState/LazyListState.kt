package com.openai.voicenote.core.ui.composeState

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember

@Composable
fun LazyListState.visibleItemsWithThreshold(
    percentThreshold: Float
): List<Int> {
    return remember(this) {
        derivedStateOf {
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (layoutInfo.totalItemsCount == 0) {
                emptyList()
            } else {
                val fullyVisibleItemsInfo = visibleItemsInfo.toMutableList()
                val lastItem = fullyVisibleItemsInfo.last()
                val viewportWidth = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset
                if (lastItem.offset + (lastItem.size * percentThreshold) > viewportWidth) {
                    fullyVisibleItemsInfo.removeLast()
                }
                val firstItem = fullyVisibleItemsInfo.first()
                if (firstItem.offset < 0) {
                    fullyVisibleItemsInfo.removeFirst()
                }
                fullyVisibleItemsInfo.map { it.index }
            }
        }
    }.value
}