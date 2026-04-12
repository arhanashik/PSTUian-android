package com.workfort.pstuian.view.ui.common.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Modifier.bgCircle(
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    borderColor: Color = MaterialTheme.colorScheme.outline,
): Modifier = this
    .clip(CircleShape)
    .background(backgroundColor)
    .border(1.dp, borderColor, CircleShape)

@Composable
fun Modifier.bgWarning(): Modifier = this
    .clip(RoundedCornerShape(5.dp))
    .background(Color(0xFFFFEBEE)) // md_red_50
    .border(2.dp, Color(0xFFEF9A9A), RoundedCornerShape(5.dp)) // md_red_200

@Composable
fun Modifier.bgDividerTxt(
    backgroundColor: Color = Color.Transparent,
    borderColor: Color = Color.LightGray,
): Modifier = this
    .clip(RoundedCornerShape(24.dp))
    .background(backgroundColor)
    .border(1.dp, borderColor, RoundedCornerShape(24.dp))

@Composable
fun Modifier.btnBgDefault(
    primaryColor: Color = Color(0xFF00796B), // colorPrimary
    primaryDarkColor: Color = Color(0xFF004D40), // colorPrimaryDark
): Modifier = this
    .clip(RoundedCornerShape(10.dp))
    .background(
        Brush.verticalGradient(
            colors = listOf(primaryDarkColor, primaryColor, primaryDarkColor)
        )
    )

@Composable
fun Modifier.btnBgPressed(
    primaryColor: Color = Color(0xFF00796B),
): Modifier = this
    .clip(RoundedCornerShape(10.dp))
    .background(primaryColor)

@Composable
fun Modifier.indicatorDot(
    color: Color = Color.Black,
): Modifier = this
    .clip(CircleShape)
    .background(color)

@Composable
fun Modifier.facultyTitleBgOverlay(): Modifier = this
    .background(
        Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color.White)
        )
    )

@Composable
fun Modifier.sliderBgOverlay(): Modifier = this
    .background(
        Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color.Black)
        )
    )

@Composable
fun Modifier.bgShadow(): Modifier = this
    .background(
        Brush.verticalGradient(
            colors = listOf(Color(0xFF00574B), Color(0xAA008577), Color.Transparent)
        )
    )

@Composable
fun Modifier.bgLabelBadge(
    backgroundColor: Color = Color.LightGray.copy(alpha = 0.4f)
): Modifier = this
    .clip(CircleShape)
    .background(backgroundColor)
    .padding(horizontal = 8.dp, vertical = 2.dp)
