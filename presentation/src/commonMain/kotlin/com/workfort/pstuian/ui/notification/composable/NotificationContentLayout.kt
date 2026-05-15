package com.workfort.pstuian.ui.notification.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.Notification
import com.workfort.pstuian.featuredomain.model.SystemNotificationDisplayType
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.AppSnackbarHost
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.notification.displaydata.NotificationDisplayData
import com.workfort.pstuian.ui.notification.displaydata.NotificationTab
import com.workfort.pstuian.ui.notification.state.NotificationUiEvent
import com.workfort.pstuian.ui.notification.state.NotificationUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NotificationContentLayout(
    uiState: NotificationUiState,
    snackbarHostState: SnackbarHostState,
    onEvent: (NotificationUiEvent) -> Unit,
) {
    AppScaffold (
        topBar = {
            AppBar(
                title = "Notifications",
                navigation = {
                    NavigationButton { onEvent(NotificationUiEvent.BackClicked) }
                },
            )
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) }
    ) {
        NotificationContent(uiState, onEvent)
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationLoadingPreview() {
    NotificationContentLayout(
        uiState = NotificationUiState(
            groupedSystemNotifications = emptyMap(),
            groupedCustomNotifications = emptyMap(),
            isLoading = true
        ),
        snackbarHostState = remember { SnackbarHostState() },
        onEvent = {},
    )
}

@Preview(showBackground = true)
@Composable
fun NotificationEmptyPreview() {
    NotificationContentLayout(
        uiState = NotificationUiState(
            groupedSystemNotifications = emptyMap(),
            groupedCustomNotifications = emptyMap(),
            isLoading = false
        ),
        snackbarHostState = remember { SnackbarHostState() },
        onEvent = {},
    )
}

@Preview(showBackground = true)
@Composable
fun SystemNotificationContentPreview() {
    NotificationContentLayout(
        uiState = NotificationUiState(
            selectedTabIndex = NotificationTab.SYSTEM,
            groupedSystemNotifications = mapOf(
                "Today" to listOf(
                    NotificationDisplayData(
                        notification = Notification.SystemNotification(
                            id = "1",
                            title = "Welcome",
                            body = "Start using the app!",
                            linkText = null,
                            link = null,
                            readAt = 1700000000000L,
                            createdAt = 1700000000000L,
                            showIn = SystemNotificationDisplayType.NONE,
                            requireSignIn = false,
                        ),
                        formattedReadAt = "10:30",
                        formattedTime = "10:30",
                        formattedDate = "15 Nov 2023"
                    ),
                    NotificationDisplayData(
                        notification = Notification.SystemNotification(
                            id = "2",
                            title = "New Feature",
                            body = "We have added a new notification system to keep you updated.",
                            linkText = null,
                            link = null,
                            readAt = 1700000000000L,
                            createdAt = 1700000000000L,
                            showIn = SystemNotificationDisplayType.NONE,
                            requireSignIn = false,
                        ),
                        formattedReadAt = "10:30",
                        formattedTime = "14:15",
                        formattedDate = "24 Jul 2023"
                    )
                )
            ),
            isLoading = false
        ),
        snackbarHostState = remember { SnackbarHostState() },
        onEvent = {},
    )
}

@Preview(showBackground = true)
@Composable
fun CustomNotificationContentPreview() {
    NotificationContentLayout(
        uiState = NotificationUiState(
            selectedTabIndex = NotificationTab.CUSTOM,
            // TODO add notification dummy data
            isLoading = false
        ),
        snackbarHostState = remember { SnackbarHostState() },
        onEvent = {},
    )
}