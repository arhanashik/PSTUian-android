package com.workfort.pstuian.ui.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.lbl_profile_offline
import pstuian.feature_presentation.generated.resources.lbl_profile_online

private val StatusPillShape = RoundedCornerShape(999.dp)

/** Light gray for offline badge (distinct from online primary). */
private val OfflineStatusLightGray = Color(0xFFD6D6D6)

/**
 * Pill label showing Online / Offline from [isOnline].
 * Used under profile avatars and on check-in list cards.
 */
@Composable
fun OnlineOfflineStatusLabel(
    isOnline: Boolean,
    modifier: Modifier = Modifier,
) {
    val badgeColor = if (isOnline) MaterialTheme.colorScheme.primary else OfflineStatusLightGray
    Box(
        modifier = modifier
            .clip(StatusPillShape)
            .background(color = badgeColor, shape = StatusPillShape)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(if (isOnline) Res.string.lbl_profile_online else Res.string.lbl_profile_offline),
            style = TextStyle.label3.copy(color = AppColors.onPrimary),
        )
    }
}
