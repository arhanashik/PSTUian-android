package com.workfort.pstuian.ui.settings.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.composable.localizedLabel
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import com.workfort.pstuian.ui.settings.state.AppPreferencePanelData
import com.workfort.pstuian.ui.settings.state.DebugPanelData
import com.workfort.pstuian.ui.settings.state.AccountPreferencesData
import com.workfort.pstuian.ui.settings.state.SettingsUiEvent
import com.workfort.pstuian.ui.settings.state.SettingsUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_user_type_row
import pstuian.feature_presentation.generated.resources.txt_sign_out
import pstuian.feature_presentation.generated.resources.txt_visitor

@Composable
fun SettingsContentPanel(
    uiState: SettingsUiState,
    onUiEvent: (SettingsUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            when (uiState) {
                is SettingsUiState.None -> Unit
                is SettingsUiState.Content -> {
                    AppPreferencesSettings(
                        appPreferencePanelData = uiState.appPreferencePanelData,
                        onClickTheme = { onUiEvent(SettingsUiEvent.ThemeClicked) },
                        onNotificationChange = { onUiEvent(SettingsUiEvent.ShowNotificationToggled(it)) },
                    )
                    AccountPreferencesSettings(
                        accountPreferencesData = uiState.accountPreferencesData,
                        onClickUserType = { onUiEvent(SettingsUiEvent.UserTypeClicked) },
                        onClearCache = { onUiEvent(SettingsUiEvent.ClearCacheClicked) },
                    )
                    if (uiState.debugPanelData != null) {
                        DebugPanelSettings(
                            debugPanelData = uiState.debugPanelData,
                            onDebugApiServerClicked = {
                                onUiEvent(SettingsUiEvent.DebugApiServerClicked)
                            },
                            onRefreshFcmToken = { onUiEvent(SettingsUiEvent.RefreshFcmTokenClicked) },
                            onForceSignOut = { onUiEvent(SettingsUiEvent.ForceSignOutClicked) },
                        )
                    }
                }
            }
        }
        if (uiState is SettingsUiState.Content) {
            SettingsFooter(
                appVersionName = uiState.appVersionName,
                appVersionCode = uiState.appVersionCode,
                deviceId = uiState.deviceId,
            )
        }
    }
}

@Composable
private fun AppPreferencesSettings(
    appPreferencePanelData: AppPreferencePanelData,
    onClickTheme: () -> Unit,
    onNotificationChange: (Boolean) -> Unit,
) {
    SettingsPanel(title = "App Preference") {
        SettingsContentRowWithActionLabel(
            label = "App Theme",
            actionLabel = appPreferencePanelData.theme.name,
            onClickAction = onClickTheme,
        )
        Spacer(modifier = Modifier.height(8.dp))
        SettingsContentRowWithSwitch(
            label = "Show Notification",
            isChecked = appPreferencePanelData.showNotification,
            onCheckedChange = onNotificationChange,
        )
    }
}

@Composable
private fun AccountPreferencesSettings(
    accountPreferencesData: AccountPreferencesData,
    onClickUserType: () -> Unit,
    onClearCache: () -> Unit,
) {
    val userTypeLabel: String = accountPreferencesData.userType?.localizedLabel() ?: stringResource(Res.string.txt_visitor)
    SettingsPanel(title = "Account Preference") {
        SettingsContentRowWithActionLabel(
            label = stringResource(Res.string.label_user_type_row),
            actionLabel = userTypeLabel,
            onClickAction = onClickUserType,
        )
        Spacer(modifier = Modifier.height(8.dp))
        SettingsContentRowWithActionButton(
            label = "Clear Cache",
            actionLabel = "Clear",
            onClickAction = onClearCache,
        )
    }
}

@Composable
private fun DebugPanelSettings(
    debugPanelData: DebugPanelData,
    onDebugApiServerClicked: () -> Unit,
    onRefreshFcmToken: () -> Unit,
    onForceSignOut: () -> Unit,
) {
    SettingsPanel(title = "Debug Panel") {
        SettingsContentRowWithActionLabel(
            label = "API server",
            actionLabel = debugPanelData.debugApiEnvironment.displayLabel,
            onClickAction = onDebugApiServerClicked,
        )
        Spacer(modifier = Modifier.height(8.dp))
        SettingsContentRowWithIconButton(
            label = "FCM Token",
            actionLabel = debugPanelData.fcmToken,
            onClickAction = onRefreshFcmToken,
        )
        Spacer(modifier = Modifier.height(8.dp))
        SettingsContentRowWithActionButton(
            label = "Force Sign Out",
            actionLabel =stringResource(Res.string.txt_sign_out),
            onClickAction = onForceSignOut,
        )
    }
}

@Composable
private fun SettingsFooter(
    appVersionName: String,
    appVersionCode: Int,
    deviceId: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "$appVersionName ($appVersionCode)",
            style = TextStyle.label3.copy(color = AppColors.textSecondary),
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = deviceId,
            style = TextStyle.label3.copy(color = AppColors.textSecondary),
        )
    }
}