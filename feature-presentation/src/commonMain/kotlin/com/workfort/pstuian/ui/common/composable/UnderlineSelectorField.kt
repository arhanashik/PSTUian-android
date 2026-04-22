package com.workfort.pstuian.ui.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle

/**
 * Matches [com.workfort.pstuian.ui.signin.composable.AuthUnderlinedField] spacing (tighter value row than raw Text).
 */
@Composable
fun UnderlineSelectorField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    trailingIcon: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
    supportingText: String? = null,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val lineColor = when {
        !enabled -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.38f)
        isError -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.outlineVariant
    }
    val lineThickness = 1.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick),
    ) {
        Text(
            text = label,
            style = TextStyle.label2.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = value,
                style = TextStyle.body1.copy(
                    lineHeight = 20.sp,
                    color = if (enabled) {
                        AppColors.textPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    },
                ),
                maxLines = 1,
                modifier = Modifier.weight(1f),
            )
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier.alpha(if (enabled) 1f else 0.38f),
                ) {
                    trailingIcon()
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(lineThickness)
                .background(lineColor),
        )
        supportingText?.takeIf { it.isNotEmpty() }?.let { msg ->
            Text(
                text = msg,
                style = TextStyle.body3.copy(
                    color = if (isError) {
                        MaterialTheme.colorScheme.error
                    } else {
                        AppColors.textSecondary
                    },
                ),
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}
