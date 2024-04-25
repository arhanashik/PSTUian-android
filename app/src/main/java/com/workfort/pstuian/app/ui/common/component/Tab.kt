package com.workfort.pstuian.app.ui.common.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp


@Composable
fun TabView(
    modifier: Modifier = Modifier,
    tabs: List<String>,
    selectedTabIndex: Int,
    onClickTab: (index: Int) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = Color.Black,
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title.toUpperCase(Locale.current)) },
                    selected = selectedTabIndex == index,
                    onClick = { onClickTab(index) }
                )
            }
        }
    }
}

@Composable
fun ScrollableTabView(
    modifier: Modifier = Modifier,
    tabs: List<String>,
    selectedTabIndex: Int,
    onClickTab: (index: Int) -> Unit,
) {
    ScrollableTabRow(
        modifier = modifier.fillMaxWidth(),
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.White,
        contentColor = Color.Black,
        edgePadding = 16.dp,
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(text = { Text(title.toUpperCase(Locale.current)) },
                selected = selectedTabIndex == index,
                onClick = { onClickTab(index) }
            )
        }
    }
}