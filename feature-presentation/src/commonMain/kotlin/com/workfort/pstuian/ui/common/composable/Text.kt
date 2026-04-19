package com.workfort.pstuian.ui.common.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.ui.common.theme.AppColors

@Composable
fun TitleText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 32.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    color: Color = MaterialTheme.colorScheme.primary,
) = Text(
    modifier = modifier,
    text = text,
    fontSize = fontSize,
    fontWeight = fontWeight,
    color = color,
)

@Composable
fun TitleTextMedium(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 24.sp,
    fontWeight: FontWeight = FontWeight.Medium,
    color: Color = AppColors.textPrimary,
) = Text(
    modifier = modifier,
    text = text,
    fontSize = fontSize,
    fontWeight = fontWeight,
    color = color,
)

@Composable
fun TitleTextSmall(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 18.sp,
    fontWeight: FontWeight = FontWeight.Medium,
    color: Color = AppColors.textSecondary,
) = Text(
    modifier = modifier,
    text = text,
    fontSize = fontSize,
    fontWeight = fontWeight,
    color = color,
)

@Composable
fun LabelText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 12.sp,
    color: Color = Color.Gray,
    fontWeight: FontWeight = FontWeight.Normal,
) = Text(
    modifier = modifier,
    text = text,
    fontSize = fontSize,
    fontWeight = fontWeight,
    color = color,
)

@Composable
fun ErrorText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 16.sp,
) = Text(
    modifier = modifier,
    text = text,
    fontSize = fontSize,
    fontWeight = FontWeight.Normal,
    color = MaterialTheme.colorScheme.secondary,
)