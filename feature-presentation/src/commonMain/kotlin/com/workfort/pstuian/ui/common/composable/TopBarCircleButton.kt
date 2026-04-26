package com.workfort.pstuian.ui.common.composable

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.theme.AppColors

@Composable
fun TopBarCircleButton(
    icon: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit,
) {
    FilledIconButton(
        onClick = onClick,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = AppColors.card,
            contentColor = AppColors.textPrimary,
        ),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(18.dp),
        )
    }
}
