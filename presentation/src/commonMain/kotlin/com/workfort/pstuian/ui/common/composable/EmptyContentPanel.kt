package com.workfort.pstuian.ui.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle

@Composable
internal fun EmptyContentPanel(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.AutoMirrored.Default.ReceiptLong,
    actionButtonText: String? = null,
    actionButtonIcon: ImageVector = Icons.Default.Add,
    onActionButtonClicked: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(AppColors.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = AppColors.primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            style = TextStyle.title2,
            color = AppColors.textPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            style = TextStyle.body2,
            color = AppColors.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        if (actionButtonText != null) {
            Spacer(modifier = Modifier.height(32.dp))
            ActionButton(
                label = actionButtonText,
                icon = actionButtonIcon,
                onClick = onActionButtonClicked,
            )
        }
    }
}
