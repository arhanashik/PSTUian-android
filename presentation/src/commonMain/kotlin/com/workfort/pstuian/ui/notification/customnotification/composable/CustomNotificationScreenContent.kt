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
import com.workfort.pstuian.featuredomain.model.UserType
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

private fun mockCustomNotificationDisplayData(
    id: String,
    title: String,
    body: String,
    readAt: Long,
    formattedTime: String,
    fromUserName: String?,
    fromUserImageUrl: String?,
    category: NotificationCategory = NotificationCategory.NEW_FOLLOWER,
) = NotificationDisplayData(
    notification = Notification.CustomNotification(
        id = id,
        title = title,
        body = body,
        linkText = null,
        link = null,
        readAt = readAt,
        createdAt = 1_700_000_000_000L,
        category = category,
        fromUserId = id.filter { it.isDigit() }.toIntOrNull() ?: id.hashCode(),
        fromUserType = UserType.STUDENT,
        fromUserName = fromUserName,
        fromUserImageUrl = fromUserImageUrl,
        toUserId = 2,
        toUserType = UserType.STUDENT,
        updatedAt = 1_700_000_000_000L,
    ),
    formattedReadAt = "",
    formattedTime = formattedTime,
    formattedDate = "15 Nov 2023",
)

private fun mockGroupedNotifications() = mapOf(
    "Today" to listOf(
        mockCustomNotificationDisplayData(
            id = "1",
            title = "New follower",
            body = "Alex started following you.",
            readAt = 0L,
            formattedTime = "14:15",
            fromUserName = "Alex Morgan",
            fromUserImageUrl = null,
        ),
        mockCustomNotificationDisplayData(
            id = "2",
            title = "Direct message",
            body = "You have a new message waiting in your inbox.",
            readAt = 0L,
            formattedTime = "13:02",
            fromUserName = null,
            fromUserImageUrl = null,
        ),
        mockCustomNotificationDisplayData(
            id = "3",
            title = "Blood donation",
            body = "Someone nearby needs your blood group.",
            readAt = 1L,
            formattedTime = "08:40",
            fromUserName = "Pat Kim",
            fromUserImageUrl = "https://invalid.invalid/avatar.png",
            category = NotificationCategory.BLOOD_DONATION,
        ),
    ),
    "Yesterday" to listOf(
        mockCustomNotificationDisplayData(
            id = "4",
            title = "Help request",
            body = "A student requested help with a course topic.",
            readAt = 1L,
            formattedTime = "18:20",
            fromUserName = "   ",
            fromUserImageUrl = null,
            category = NotificationCategory.HELP,
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

@Preview(showBackground = true, name = "Custom - Error")
@Composable
private fun CustomNotificationScreenContentErrorPreview() {
    AppTheme {
        CustomNotificationScreenContent(
            uiState = CustomNotificationUiState.Error(error = "Could not load notifications"),
            onUiEvent = {},
        )
    }
}
