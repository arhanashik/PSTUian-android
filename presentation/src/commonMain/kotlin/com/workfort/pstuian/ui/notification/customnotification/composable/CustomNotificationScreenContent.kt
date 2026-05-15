package com.workfort.pstuian.ui.notification.customnotification.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.Notification
import com.workfort.pstuian.featuredomain.model.NotificationCategory
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.notification.common.displaydata.NotificationDisplayData
import com.workfort.pstuian.ui.notification.customnotification.state.CustomNotificationUiEvent
import com.workfort.pstuian.ui.notification.customnotification.state.CustomNotificationUiState

@Composable
internal fun CustomNotificationScreenContent(
    uiState: CustomNotificationUiState,
    onUiEvent: (CustomNotificationUiEvent) -> Unit,
) {
    when (uiState) {
        is CustomNotificationUiState.None -> Unit
        is CustomNotificationUiState.Content -> CustomNotificationContentPanel(uiState, onUiEvent)
        is CustomNotificationUiState.Error -> {
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

private fun mockGroupedNotifications() = mapOf(
    "Today" to listOf(
        NotificationDisplayData(
            notification = Notification.CustomNotification(
                id = "1",
                title = "New follower",
                body = "Someone started following you.",
                linkText = null,
                link = null,
                readAt = 0L,
                createdAt = 1700000000000L,
                category = NotificationCategory.NEW_FOLLOWER,
                fromUserId = 1,
                toUserId = 2,
                updatedAt = 1700000000000L,
            ),
            formattedReadAt = "",
            formattedTime = "14:15",
            formattedDate = "15 Nov 2023",
        ),
    ),
)

@Preview(showBackground = true, name = "Custom - Content")
@Composable
private fun CustomNotificationScreenContentPreview() {
    AppTheme {
        CustomNotificationScreenContent(
            uiState = CustomNotificationUiState.Content(
                groupedNotifications = mockGroupedNotifications(),
            ),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Custom - Loading")
@Composable
private fun CustomNotificationScreenContentLoadingPreview() {
    AppTheme {
        CustomNotificationScreenContent(
            uiState = CustomNotificationUiState.Content(
                isLoading = true,
                groupedNotifications = emptyMap(),
            ),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Custom - Loading More")
@Composable
private fun CustomNotificationScreenContentLoadingMorePreview() {
    AppTheme {
        CustomNotificationScreenContent(
            uiState = CustomNotificationUiState.Content(
                isLoading = true,
                groupedNotifications = mockGroupedNotifications(),
            ),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Custom - Empty")
@Composable
private fun CustomNotificationScreenContentEmptyPreview() {
    AppTheme {
        CustomNotificationScreenContent(
            uiState = CustomNotificationUiState.Content(
                groupedNotifications = emptyMap(),
            ),
            onUiEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "Custom - Dark")
@Composable
private fun CustomNotificationScreenContentDarkPreview() {
    AppTheme(themeMode = ThemeMode.Dark) {
        CustomNotificationScreenContent(
            uiState = CustomNotificationUiState.Content(
                groupedNotifications = mockGroupedNotifications(),
            ),
            onUiEvent = {},
        )
    }
}
