package com.workfort.pstuian.ui.notification.notification.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.notification.notification.state.NotificationUiEvent
import com.workfort.pstuian.ui.notification.notification.state.NotificationUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NotificationScreenContent(
    uiState: NotificationUiState,
    onUiEvent: (NotificationUiEvent) -> Unit,
) {
    AppScaffold(
        topBar = {
            AppBar(
                title = "Notifications",
                navigation = {
                    NavigationButton { onUiEvent(NotificationUiEvent.BackClicked) }
                },
            )
        },
    ) {
        when (uiState) {
            is NotificationUiState.None -> Unit
            is NotificationUiState.Content -> NotificationContentPanel(uiState, onUiEvent)
        }
    }
}

private fun mockUiState(selectedTab: Int = 0): NotificationUiState.Content {
    return NotificationUiState.Content(
        tabs = listOf("System", "Custom"),
        selectedTab = selectedTab,
    )
}

@Preview(showBackground = true, name = "System Tab")
@Composable
private fun NotificationScreenContentSystemTabPreview() {
    AppTheme {
        NotificationScreenContent(uiState = mockUiState(selectedTab = 0), onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "Custom Tab")
@Composable
private fun NotificationScreenContentCustomTabPreview() {
    AppTheme {
        NotificationScreenContent(uiState = mockUiState(selectedTab = 1), onUiEvent = {})
    }
}

@Preview(showBackground = true, name = "Dark - System Tab")
@Composable
private fun NotificationScreenContentDarkPreview() {
    AppTheme(themeMode = ThemeMode.Dark) {
        NotificationScreenContent(uiState = mockUiState(selectedTab = 0), onUiEvent = {})
    }
}
