package com.workfort.pstuian.ui.notification.systemnotification.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.Notification
import com.workfort.pstuian.featuredomain.model.SystemNotificationDisplayType
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.notification.common.displaydata.NotificationDisplayData
import com.workfort.pstuian.ui.notification.systemnotification.state.SystemNotificationUiEvent
import com.workfort.pstuian.ui.notification.systemnotification.state.SystemNotificationUiState

@Composable
internal fun SystemNotificationScreenContent(
    uiState: SystemNotificationUiState,
    onUiEvent: (SystemNotificationUiEvent) -> Unit,
) {
    when (uiState) {
        is SystemNotificationUiState.None -> Unit
        is SystemNotificationUiState.Loading -> SystemNotificationListShimmer()
        is SystemNotificationUiState.Content -> SystemNotificationContentPanel(uiState, onUiEvent)
        is SystemNotificationUiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedErrorView(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun SystemNotificationListShimmer() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
    ) {
        items(6) {
            Box(modifier = Modifier.padding(vertical = 6.dp)) {
                SystemNotificationShimmer()
            }
        }
    }
}

private fun mockSystemNotificationDisplayData(
    id: String,
    title: String,
    body: String,
    readAt: Long,
    formattedTime: String,
) = NotificationDisplayData(
    notification = Notification.SystemNotification(
        id = id,
        title = title,
        body = body,
        linkText = null,
        link = null,
        readAt = readAt,
        createdAt = 1_700_000_000_000L,
        showIn = SystemNotificationDisplayType.NONE,
        requireSignIn = false,
    ),
    formattedReadAt = "",
    formattedTime = formattedTime,
    formattedDate = "15 Nov 2023",
)

private fun mockGroupedNotifications() = mapOf(
    "Today" to listOf(
        mockSystemNotificationDisplayData(
            id = "1",
            title = "Welcome",
            body = "Start using the app and explore campus features.",
            readAt = 0L,
            formattedTime = "10:30",
        ),
        mockSystemNotificationDisplayData(
            id = "2",
            title = "Maintenance tonight",
            body = "Some services may be unavailable between 2:00 and 4:00 AM.",
            readAt = 0L,
            formattedTime = "09:05",
        ),
    ),
    "Yesterday" to listOf(
        mockSystemNotificationDisplayData(
            id = "3",
            title = "Privacy policy updated",
            body = "We have updated our terms. Tap to read the summary.",
            readAt = 1L,
            formattedTime = "16:45",
        ),
    ),
)

@Preview(showBackground = true, name = "System - Content")
@Composable
private fun SystemNotificationScreenContentPreview() {
    AppTheme {
        SystemNotificationScreenContent(
            uiState = SystemNotificationUiState.Content(
                groupedNotifications = mockGroupedNotifications(),
            ),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "System - Loading")
@Composable
private fun SystemNotificationScreenContentLoadingPreview() {
    AppTheme {
        SystemNotificationScreenContent(
            uiState = SystemNotificationUiState.Loading,
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "System - Empty")
@Composable
private fun SystemNotificationScreenContentEmptyPreview() {
    AppTheme {
        SystemNotificationScreenContent(
            uiState = SystemNotificationUiState.Content(
                groupedNotifications = emptyMap(),
            ),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "System - Dark")
@Composable
private fun SystemNotificationScreenContentDarkPreview() {
    AppTheme(themeMode = ThemeMode.Dark) {
        SystemNotificationScreenContent(
            uiState = SystemNotificationUiState.Content(
                groupedNotifications = mockGroupedNotifications(),
            ),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "System - Error")
@Composable
private fun SystemNotificationScreenContentErrorPreview() {
    AppTheme {
        SystemNotificationScreenContent(
            uiState = SystemNotificationUiState.Error(error = "Could not load notifications"),
            onUiEvent = {},
        )
    }
}
