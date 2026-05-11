package com.workfort.pstuian.ui.profile.common.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.composable.TopBarCircleButton
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import org.jetbrains.compose.resources.stringResource
import pstuian.presentation.generated.resources.Res
import pstuian.presentation.generated.resources.txt_go_back

@Composable
internal fun ProfileTopBar(
    title: String,
    onNavigationBack: () -> Unit,
    profileDropdown: @Composable (expanded: Boolean, onDismiss: () -> Unit) -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        TopBarCircleButton(
            icon = Icons.AutoMirrored.Filled.ArrowBackIos,
            contentDescription = stringResource(Res.string.txt_go_back),
            onClick = onNavigationBack,
        )

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = TextStyle.title3,
            fontWeight = FontWeight.Bold,
            color = AppColors.textPrimary,
            textAlign = TextAlign.Center,
        )

        Box {
            TopBarCircleButton(
                icon = Icons.Filled.MoreHoriz,
                onClick = { menuExpanded = true },
            )
            profileDropdown(menuExpanded) { menuExpanded = false }
        }
    }
}
