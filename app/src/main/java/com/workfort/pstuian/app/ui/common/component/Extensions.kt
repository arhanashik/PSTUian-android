package com.workfort.pstuian.app.ui.common.component

import androidx.compose.foundation.lazy.LazyListState


val LazyListState.isLastItemVisible: Boolean
    get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1