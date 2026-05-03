package com.workfort.pstuian.ui.settings.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle


@Composable
internal fun SettingsCardHeader(title: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = TextStyle.title3.copy(color = AppColors.textPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

@Composable
internal fun SettingsContentRowWithActionLabel(
    label: String,
    actionLabel: String,
    onClickAction: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = settingsCardLabelStyle(),
        )
        Text(
            text = actionLabel,
            style = settingsCardActionLabelStyle(),
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(AppColors.BrandYellow.copy(alpha = 0.42f))
                .clickable(onClick = onClickAction)
                .padding(horizontal = 10.dp, vertical = 5.dp),
        )
    }
}

@Composable
internal fun SettingsContentRowWithActionButton(
    label: String,
    actionLabel: String,
    onClickAction: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, style = settingsCardLabelStyle())
        Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
            OutlinedButton(
                onClick = onClickAction,
                modifier = Modifier.height(30.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
            ) {
                Text(text = actionLabel, style = settingsCardActionLabelStyle())
            }
        }
    }
}

@Composable
internal fun SettingsContentRowWithSwitch(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, style = settingsCardLabelStyle())
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            thumbContent = {
                if (isChecked) {
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

@Composable
internal fun SettingsContentRowWithIconButton(
    label: String,
    actionLabel: String,
    icon: ImageVector = Icons.Default.Refresh,
    onClickAction: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = label, style = settingsCardLabelStyle())
            IconButton(onClick = onClickAction) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = AppColors.textSecondary,
                )
            }
        }
        Text(text = actionLabel, style = settingsCardActionLabelStyle())
    }
}

@Composable
internal fun SettingsFooter(
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

@Composable
private fun settingsCardLabelStyle() = TextStyle.label1.copy(
    fontWeight = FontWeight.SemiBold,
    color = AppColors.textPrimary,
)

@Composable
private fun settingsCardActionLabelStyle() = TextStyle.label2.copy(
    fontWeight = FontWeight.SemiBold,
    color = AppColors.textSecondary,
)