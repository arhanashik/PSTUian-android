package com.workfort.pstuian.app.ui.common.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex


@Composable
fun MaterialButtonToggleGroup(
    modifier: Modifier = Modifier,
    items: List<String>,
    selectedIndex: Int? = null,
    cornerRadius: Dp = 8.dp,
    onClick: (index: Int) -> Unit,
) {
    LaunchedEffect(key1 = null) {
        selectedIndex?.let(onClick)
    }
    Row(modifier = modifier) {
        items.forEachIndexed { index, item ->
            OutlinedButton(
                modifier = Modifier
                    .offset(if (index == 0) 0.dp else (-1 * index).dp, 0.dp)
                    .zIndex(if (selectedIndex == index) 1f else 0f),
                onClick = { onClick(index) },
                shape = when (index) {
                    // left outer button
                    0 -> RoundedCornerShape(
                        topStart = cornerRadius,
                        topEnd = 0.dp,
                        bottomStart = cornerRadius,
                        bottomEnd = 0.dp
                    )
                    // right outer button
                    items.size - 1 -> RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = cornerRadius,
                        bottomStart = 0.dp,
                        bottomEnd = cornerRadius
                    )
                    // middle button
                    else -> RoundedCornerShape(0.dp)
                },
                border = BorderStroke(
                    1.dp, if (selectedIndex == index) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.LightGray
                    }
                ),
                colors = if (selectedIndex == index) {
                    // selected colors
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                } else {
                    // not selected colors
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                },
            ) {
                Text(
                    text = item,
                    color = if (selectedIndex == index) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.DarkGray.copy(alpha = 0.8f)
                    },
                )
            }
        }
    }
}

//@Preview
//@Composable
//private fun Preview() {
//    AppTheme {
//        Scaffold {
//            Column(modifier = Modifier.padding(it)) {
//                MaterialButtonToggleGroup(
//                    items = listOf("Button 1", "Button 2"),
//                    defaultSelectedIndex = 0,
//                )
//            }
//        }
//    }
//}