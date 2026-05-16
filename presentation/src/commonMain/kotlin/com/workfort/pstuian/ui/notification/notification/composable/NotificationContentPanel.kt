package com.workfort.pstuian.ui.notification.notification.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.composable.ToggleSwitch
import com.workfort.pstuian.ui.notification.common.displaydata.NotificationTab
import com.workfort.pstuian.ui.notification.customnotification.CustomNotificationScreen
import com.workfort.pstuian.ui.notification.notification.state.NotificationUiEvent
import com.workfort.pstuian.ui.notification.notification.state.NotificationUiState
import com.workfort.pstuian.ui.notification.systemnotification.SystemNotificationScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NotificationContentPanel(
    uiState: NotificationUiState.Content,
    onUiEvent: (NotificationUiEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { uiState.tabs.size })

    LaunchedEffect(uiState.selectedTab) {
        if (pagerState.currentPage != uiState.selectedTab) {
            pagerState.scrollToPage(uiState.selectedTab)
        }
    }

    LaunchedEffect(pagerState.settledPage) {
        if (uiState.selectedTab != pagerState.settledPage) {
            onUiEvent(NotificationUiEvent.SelectTab(pagerState.settledPage))
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ToggleSwitch(
            options = uiState.tabs,
            selectedIndex = uiState.selectedTab,
            modifier = Modifier.padding(16.dp),
            onSelectedIndexChange = { index ->
                scope.launch { pagerState.animateScrollToPage(index) }
                onUiEvent(NotificationUiEvent.SelectTab(index))
            },
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            when (NotificationTab.entries[page]) {
                NotificationTab.SYSTEM -> SystemNotificationScreen()
                NotificationTab.CUSTOM -> CustomNotificationScreen()
            }
        }
    }
}
