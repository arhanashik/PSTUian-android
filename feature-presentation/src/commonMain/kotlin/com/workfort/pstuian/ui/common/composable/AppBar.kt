package com.workfort.pstuian.ui.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AppBar(
    title: String?,
    navigation: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = { },
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    Surface(
        shadowElevation = 2.dp,
        tonalElevation = 2.dp,
        color = AppColors.background,
    ) {
        TopAppBar(
            title = {
                title?.let {
                    Text(
                        text = it,
                        style = TextStyle.title2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 12.dp),
                    )
                }
            },
            navigationIcon = { navigation?.invoke() },
            actions = {
                actions()
                Spacer(modifier = Modifier.width(16.dp))
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.background),
            modifier = modifier,
            scrollBehavior = scrollBehavior,
        )
    }
}

@Composable
fun NavigationButton(
    icon: ImageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
    onClickNavigation: () -> Unit,
) {
    IconButton(onClick = onClickNavigation) {
        Icon(imageVector = icon, contentDescription = "NavigationButton")
    }
}

@Composable
fun AppBarIconButton(
    icon: ImageVector,
    contentDescription: String = "",
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badge: @Composable (BoxScope.() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .size(32.dp)
            .clip(CircleShape)
            .background(AppColors.primary.copy(alpha = 0.15f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Box {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = AppColors.primary,
                modifier = Modifier.size(18.dp),
            )
            badge?.invoke(this)
        }
    }
}