package com.workfort.pstuian.ui.settings.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.workfort.pstuian.featuredomain.model.DebugApiEnvironment
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.ListSelectionBottomSheet
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.composable.debugApiEnvironmentListSelectionOptions
import com.workfort.pstuian.ui.common.composable.themeModeListSelectionOptions
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.settings.state.DebugPanelData
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
    var showThemeBottomSheet by remember { mutableStateOf(false) }
    var showApiServerBottomSheet by remember { mutableStateOf(false) }

    AppScaffold(
        topBar = {
            AppBar(
                title = stringResource(Res.string.label_settings_screen),
                navigation = {
                    NavigationButton { onUiEvent(SettingsUiEvent.BackClicked) }
                },
            )
        },
    ) {
        SettingsContentPanel(uiState) { event ->
            when (event) {
                SettingsUiEvent.ThemeClicked -> showThemeBottomSheet = true
                SettingsUiEvent.DebugApiServerClicked -> showApiServerBottomSheet = true
                else -> onUiEvent(event)
            }
        }
    }

    val contentState = uiState as? SettingsUiState.Content
    val currentTheme = contentState?.theme
    if (showThemeBottomSheet && currentTheme != null) {
        ListSelectionBottomSheet(
            title = "Select App Theme",
            helperText = "Choose how the app should appear.",
            primaryButtonLabel = "Apply Theme",
            options = themeModeListSelectionOptions(),
            initialSelection = currentTheme,
            scrollable = false,
            onDismiss = { showThemeBottomSheet = false },
            onConfirm = { selectedTheme ->
                selectedTheme?.let { onUiEvent(SettingsUiEvent.ChangeThemeClicked(it)) }
                showThemeBottomSheet = false
            },
        )
    }
    val currentDebugEnv = contentState?.debugPanelData?.debugApiEnvironment
    if (showApiServerBottomSheet && currentDebugEnv != null) {
        ListSelectionBottomSheet(
            title = "Select API server",
            helperText = "Choose which backend the app uses in debug builds.",
            primaryButtonLabel = "Apply",
            options = debugApiEnvironmentListSelectionOptions(),
            initialSelection = currentDebugEnv,
            scrollable = false,
            onDismiss = { showApiServerBottomSheet = false },
            onConfirm = { selected ->
                selected?.let { onUiEvent(SettingsUiEvent.DebugApiEnvironmentSelected(it)) }
                showApiServerBottomSheet = false
            },
        )
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
    userType = UserType.STUDENT,
    showNotification = true,
    theme = ThemeMode.System,
    appVersionName = "3.0.0",
    appVersionCode = 7,
    deviceId = "mock-device-id",
    debugPanelData = DebugPanelData(
        fcmToken = "mock_fcm_token",
        debugApiEnvironment = DebugApiEnvironment.LOCAL,
    ),
)
