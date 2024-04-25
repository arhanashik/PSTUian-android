package com.workfort.pstuian.app.ui.common.component

import android.widget.TextView
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

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
    color: Color = Color.Black,
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
    color: Color = Color.Black,
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

@Composable
fun HtmlText(modifier: Modifier = Modifier, html: String) {
    AndroidView(
        modifier = modifier,
        factory = { context -> TextView(context) },
        update = { it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT) }
    )
}