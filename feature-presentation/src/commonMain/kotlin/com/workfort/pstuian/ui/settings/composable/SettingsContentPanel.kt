package com.workfort.pstuian.ui.settings.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import com.workfort.pstuian.ui.settings.state.SettingsUiEvent
import com.workfort.pstuian.ui.settings.state.SettingsUiState

@Composable
fun SettingsContentPanel(
    uiState: SettingsUiState,
    onUiEvent: (SettingsUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        when (uiState) {
            is SettingsUiState.None -> Unit
            is SettingsUiState.Content -> {
                GeneralSettingsView(
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
}

@Composable
private fun GeneralSettingsView(
    currentTheme: ThemeMode,
    showNotification: Boolean,
    onThemeChange: (ThemeMode) -> Unit,
    onNotificationChange: (Boolean) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = "App Theme", style = TextStyle.title3.copy(color = AppColors.textPrimary))
                Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
                    OutlinedButton(onClick = { expanded = true }) {
                        Text(text = currentTheme.name, style = TextStyle.label1.copy(color = AppColors.primary))
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
                Text(text = "Show Notification", style = TextStyle.title3.copy(color = AppColors.textPrimary))
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
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ElevatedCard(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "FCM Token",
                        style = TextStyle.title3.copy(color = AppColors.textPrimary),
                    )
                    IconButton(onClick = onRefreshFcmToken) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = AppColors.primary,
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
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
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
                    style = TextStyle.title3.copy(color = AppColors.textPrimary),
                )
                OutlinedButton(onClick = onClearPrefs) {
                    Text(text = "Clear", style = TextStyle.label1.copy(color = AppColors.error))
                }
            }
        }
    }
}
