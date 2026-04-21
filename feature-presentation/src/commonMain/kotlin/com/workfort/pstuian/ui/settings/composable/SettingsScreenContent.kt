package com.workfort.pstuian.ui.settings.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.AppUsageRole
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.settings.state.SettingsUiEvent
import com.workfort.pstuian.ui.settings.state.SettingsUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_settings_screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreenContent(
    uiState: SettingsUiState,
    onUiEvent: (SettingsUiEvent) -> Unit,
) {
    AppScaffold(
        topBar = {
            AppBar(
                title = stringResource(Res.string.label_settings_screen),
                navigation = {
                    NavigationButton { onUiEvent(SettingsUiEvent.OnClickBack) }
                },
            )
        },
    ) {
        SettingsContentPanel(uiState, onUiEvent)
    }
}

@Preview
@Composable
fun SettingsScreenContentPreview() {
    AppTheme {
        SettingsScreenContent(
            uiState = mockUiSate,
            onUiEvent = {},
        )
    }
}

@Preview
@Composable
fun SettingsScreenContentDarkPreview() {
    AppTheme(theme = ThemeMode.Dark) {
        SettingsScreenContent(
            uiState = mockUiSate,
            onUiEvent = {},
        )
    }
}

private val mockUiSate = SettingsUiState.Content(
    showNotification = true,
    theme = ThemeMode.System,
    isDebug = true,
    fcmToken = "mock_fcm_token",
    appUsageRole = AppUsageRole.TEACHER,
    appVersionName = "3.0.0",
    appVersionCode = 7,
    deviceId = "mock-device-id",
)
