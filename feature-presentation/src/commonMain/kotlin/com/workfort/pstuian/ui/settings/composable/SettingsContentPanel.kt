package com.workfort.pstuian.ui.settings.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.composable.localizedLabel
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import com.workfort.pstuian.ui.settings.state.DebugPanelData
import com.workfort.pstuian.ui.settings.state.SettingsUiEvent
import com.workfort.pstuian.ui.settings.state.SettingsUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_user_type_row
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
                        userType = uiState.userType,
                        onClickUserType = { onUiEvent(SettingsUiEvent.UserTypeClicked) },
                        currentTheme = uiState.theme,
                        showNotification = uiState.showNotification,
                        onClickTheme = { onUiEvent(SettingsUiEvent.ThemeClicked) },
                        onNotificationChange = { onUiEvent(SettingsUiEvent.ShowNotificationToggled(it)) },
                    )
                    if (uiState.debugPanelData != null) {
                        DebugSettingsView(
                            debugPanelData = uiState.debugPanelData,
                            onRefreshFcmToken = { onUiEvent(SettingsUiEvent.RefreshFcmTokenClicked) },
                            onClearCache = { onUiEvent(SettingsUiEvent.ClearCacheClicked) },
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
private fun settingsCardLabelStyle() = TextStyle.title3.copy(
    fontSize = 15.sp,
    fontWeight = FontWeight.SemiBold,
    color = AppColors.textPrimary,
)

@Composable
private fun GeneralSettingsView(
    userType: UserType?,
    onClickUserType: () -> Unit,
    currentTheme: ThemeMode,
    showNotification: Boolean,
    onClickTheme: () -> Unit,
    onNotificationChange: (Boolean) -> Unit,
) {
    val userTypeLabel: String = userType?.localizedLabel() ?: stringResource(Res.string.txt_visitor)
    val rowLabelStyle = settingsCardLabelStyle()
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            // User Type
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(Res.string.label_user_type_row),
                    style = rowLabelStyle,
                )
                Text(
                    text = userTypeLabel,
                    style = TextStyle.label1.copy(
                        color = AppColors.textPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AppColors.BrandYellow.copy(alpha = 0.42f))
                        .clickable(onClick = onClickUserType)
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                )
            }

            // App Theme
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = "App Theme", style = rowLabelStyle)
                Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
                    OutlinedButton(
                        onClick = onClickTheme,
                        modifier = Modifier.height(30.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    ) {
                        Text(
                            text = currentTheme.name,
                            style = TextStyle.label2.copy(
                                color = AppColors.textSecondary,
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.sp,
                            ),
                        )
                    }
                }
            }

            // Show Notification
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = "Show Notification", style = rowLabelStyle)
                Switch(
                    checked = showNotification,
                    onCheckedChange = { onNotificationChange(it) },
                    thumbContent = {
                        if (showNotification) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        uncheckedBorderColor = Color.LightGray,
                    ),
                )
            }
        }
    }
}

@Composable
private fun DebugSettingsView(
    debugPanelData: DebugPanelData,
    onRefreshFcmToken: () -> Unit,
    onClearCache: () -> Unit,
) {
    val rowLabelStyle = settingsCardLabelStyle()
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ElevatedCard(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "FCM Token",
                        style = rowLabelStyle,
                    )
                    IconButton(onClick = onRefreshFcmToken) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = AppColors.textSecondary,
                        )
                    }
                }
                Text(
                    text = debugPanelData.fcmToken,
                    style = TextStyle.body2.copy(color = AppColors.textSecondary),
                )
            }
        }

        ElevatedCard(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Clear Cache",
                    style = rowLabelStyle,
                )
                OutlinedButton(
                    onClick = onClearCache,
                    modifier = Modifier.height(30.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = "Clear",
                        style = TextStyle.label2.copy(
                            color = AppColors.error,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp,
                        ),
                    )
                }
            }
        }
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
