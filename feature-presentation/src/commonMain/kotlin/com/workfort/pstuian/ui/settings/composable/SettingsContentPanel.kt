package com.workfort.pstuian.ui.settings.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.composable.localizedLabel
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.settings.state.AppPreferencePanelData
import com.workfort.pstuian.ui.settings.state.DebugPanelData
import com.workfort.pstuian.ui.settings.state.GeneralPanelData
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
                    GeneralSettingsView(
                        generalPanelData = uiState.generalPanelData,
                        onClickUserType = { onUiEvent(SettingsUiEvent.UserTypeClicked) },
                    )
                    AppPreferencesView(
                        appPreferencePanelData = uiState.appPreferencePanelData,
                        onClickTheme = { onUiEvent(SettingsUiEvent.ThemeClicked) },
                        onNotificationChange = { onUiEvent(SettingsUiEvent.ShowNotificationToggled(it)) },
                    )
                    if (uiState.debugPanelData != null) {
                        DebugSettingsView(
                            debugPanelData = uiState.debugPanelData,
                            onDebugApiServerClicked = {
                                onUiEvent(SettingsUiEvent.DebugApiServerClicked)
                            },
                            onRefreshFcmToken = { onUiEvent(SettingsUiEvent.RefreshFcmTokenClicked) },
                            onClearCache = { onUiEvent(SettingsUiEvent.ClearCacheClicked) },
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
private fun GeneralSettingsView(
    generalPanelData: GeneralPanelData,
    onClickUserType: () -> Unit,
) {
    val userTypeLabel: String = generalPanelData.userType?.localizedLabel() ?: stringResource(Res.string.txt_visitor)
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            SettingsCardHeader(title = "General")
            HorizontalDivider(color = AppColors.background)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                SettingsContentRowWithActionLabel(
                    label = stringResource(Res.string.label_user_type_row),
                    actionLabel = userTypeLabel,
                    onClickAction = onClickUserType,
                )
            }
        }
    }
}

@Composable
private fun AppPreferencesView(
    appPreferencePanelData: AppPreferencePanelData,
    onClickTheme: () -> Unit,
    onNotificationChange: (Boolean) -> Unit,
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            SettingsCardHeader(title = "App Preference")
            HorizontalDivider(color = AppColors.background)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
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
    }
}

@Composable
private fun DebugSettingsView(
    debugPanelData: DebugPanelData,
    onDebugApiServerClicked: () -> Unit,
    onRefreshFcmToken: () -> Unit,
    onClearCache: () -> Unit,
    onForceSignOut: () -> Unit,
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            SettingsCardHeader(title = "Debug Panel")
            HorizontalDivider(color = AppColors.background)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
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
                Spacer(modifier = Modifier.height(8.dp))
                SettingsContentRowWithActionButton(
                    label = "Clear Cache",
                    actionLabel = "Clear",
                    onClickAction = onClearCache,
                )
            }
        }
    }
}
