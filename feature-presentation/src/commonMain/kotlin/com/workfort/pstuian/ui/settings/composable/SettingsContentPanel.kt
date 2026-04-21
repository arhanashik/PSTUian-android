package com.workfort.pstuian.ui.settings.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.featuredomain.model.AppUsageRole
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.ui.common.composable.localizedLabel
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import com.workfort.pstuian.ui.settings.state.SettingsUiEvent
import com.workfort.pstuian.ui.settings.state.SettingsUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_settings_footer_device_id
import pstuian.feature_presentation.generated.resources.label_settings_footer_version
import pstuian.feature_presentation.generated.resources.label_user_type_not_set
import pstuian.feature_presentation.generated.resources.label_user_type_row

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
                        appUsageRole = uiState.appUsageRole,
                        onClickAppUsageRole = { onUiEvent(SettingsUiEvent.OnClickEditAppUsageRole) },
                        currentTheme = uiState.theme,
                        showNotification = uiState.showNotification,
                        onThemeChange = { onUiEvent(SettingsUiEvent.OnChangeTheme(it)) },
                        onNotificationChange = { onUiEvent(SettingsUiEvent.SetShowNotification(it)) },
                    )
                    if (uiState.isDebug) {
                        DebugSettingsView(
                            fcmToken = uiState.fcmToken,
                            onRefreshFcmToken = { onUiEvent(SettingsUiEvent.OnRefreshFcmToken) },
                            onClearPrefs = { onUiEvent(SettingsUiEvent.OnClearSharedPrefs) },
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
private fun SettingsFooter(
    appVersionName: String,
    appVersionCode: Int,
    deviceId: String,
) {
    val small = MaterialTheme.typography.bodySmall.copy(
        fontSize = 11.sp,
        lineHeight = 14.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.label_settings_footer_version, appVersionName, appVersionCode),
            style = small,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = stringResource(Res.string.label_settings_footer_device_id, deviceId),
            style = small,
        )
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
    appUsageRole: AppUsageRole?,
    onClickAppUsageRole: () -> Unit,
    currentTheme: ThemeMode,
    showNotification: Boolean,
    onThemeChange: (ThemeMode) -> Unit,
    onNotificationChange: (Boolean) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val roleLabel = appUsageRole?.localizedLabel()
        ?: stringResource(Res.string.label_user_type_not_set)

    val rowLabelStyle = settingsCardLabelStyle()
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClickAppUsageRole)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(Res.string.label_user_type_row),
                    style = rowLabelStyle,
                )
                Text(
                    text = roleLabel,
                    style = TextStyle.label1.copy(
                        color = AppColors.textPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AppColors.BrandYellow.copy(alpha = 0.42f))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                )
            }
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
                        onClick = { expanded = true },
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
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        ThemeMode.entries.forEach { theme ->
                            DropdownMenuItem(
                                text = { Text(theme.name) },
                                onClick = {
                                    onThemeChange(theme)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

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
    fcmToken: String,
    onRefreshFcmToken: () -> Unit,
    onClearPrefs: () -> Unit,
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
                    text = fcmToken,
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
                    text = "Clear SharedPreferences",
                    style = rowLabelStyle,
                )
                OutlinedButton(
                    onClick = onClearPrefs,
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
