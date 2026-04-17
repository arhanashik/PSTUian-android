package com.workfort.pstuian.ui.settings.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.composable.TitleTextSmall
import com.workfort.pstuian.ui.settings.state.SettingsUiEvent
import com.workfort.pstuian.ui.settings.state.SettingsUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.about_it
import pstuian.feature_presentation.generated.resources.data_load_policy
import pstuian.feature_presentation.generated.resources.dev_team
import pstuian.feature_presentation.generated.resources.label_contact_us

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
    ) {
        NotificationSettingsView(uiState.showNotification) {
            onUiEvent(SettingsUiEvent.SetShowNotification(it))
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))
        InfoPanel()
    }
}

@Composable
private fun NotificationSettingsView(
    showNotification: Boolean,
    onSettingsChange: (Boolean) -> Unit,
) {
    ElevatedCard(shape = RoundedCornerShape(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TitleTextSmall(text = "Show Notification")
            Switch(
                checked = showNotification,
                onCheckedChange = { onSettingsChange(it) },
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

@Composable
private fun InfoPanel() {
    Column {
        ElevatedCard(shape = RoundedCornerShape(16.dp)) {
            Text(
                text = stringResource(Res.string.about_it),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
            )
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))
        ElevatedCard(shape = RoundedCornerShape(16.dp)) {
            Text(
                text = stringResource(Res.string.data_load_policy),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
            )
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(Res.string.dev_team),
                    textAlign = TextAlign.Center,
                )
                TextButton(
                    onClick = {
                        // This event doesn't seem to be used by the machine, but was in original.
                        // We should either add it or handle navigation from screen if needed.
                    }
                ) {
                    Text(
                        text = stringResource(Res.string.label_contact_us),
                    )
                }
            }
        }
    }
}
