package com.workfort.pstuian.common.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object TextStyle {
    val title1: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp,
        )

    val title2: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp,
        )

    val title3: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp,
        )

    val body1: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp,
        )

    val body2: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp,
        )

    val body3: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp,
        )

    val label1: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp,
        )

    val label2: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
        )

    val label3: TextStyle
        @Composable
        get() = TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            letterSpacing = 0.5.sp,
        )
}