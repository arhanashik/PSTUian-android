package com.workfort.pstuian.common.composable

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState


val LazyListState.isLastItemVisible: Boolean
    get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

val LazyGridState.isLastItemVisible: Boolean
    get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1