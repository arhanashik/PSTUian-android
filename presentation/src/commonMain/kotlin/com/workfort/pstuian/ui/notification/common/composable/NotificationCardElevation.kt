package com.workfort.pstuian.ui.notification.common.composable

import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
internal fun notificationCardElevation(isUnread: Boolean) = CardDefaults.cardElevation(
    defaultElevation = if (isUnread) 2.dp else 0.dp,
    pressedElevation = if (isUnread) 2.dp else 0.dp,
    focusedElevation = if (isUnread) 2.dp else 0.dp,
    hoveredElevation = if (isUnread) 3.dp else 0.dp,
    draggedElevation = if (isUnread) 4.dp else 0.dp,
    disabledElevation = 0.dp,
)
