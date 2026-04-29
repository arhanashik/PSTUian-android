package com.workfort.pstuian.ui.signin.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun SignInHeaderContent(onSkip: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Skip",
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .clickable(onClick = onSkip),
        )
    }
}

@Composable
internal fun SignUpHeaderContent(onBack: () -> Unit) {
    CompactAuthHeader(title = "Create Account", onBack = onBack)
}

@Composable
internal fun ForgotPasswordHeaderContent(onBack: () -> Unit) {
    CompactAuthHeader(title = "Forgot Password", onBack = onBack)
}

@Composable
internal fun EmailVerificationHeaderContent(onBack: () -> Unit) {
    CompactAuthHeader(title = "Email Verification", onBack = onBack)
}

@Composable
internal fun CompactAuthHeader(title: String, onBack: () -> Unit) {
    val layoutDirection = LocalLayoutDirection.current
    // ArrowBackIos has extra empty space on the trailing edge; nudge toward center in the circle.
    val backIconOffsetX = when (layoutDirection) {
        LayoutDirection.Rtl -> (-3).dp
        else -> 3.dp
    }
    // Sit the back-row at the top of the green section so it stays pinned regardless of how
    // tall the header grows (e.g. when the ForgotPassword form is short the green area can be
    // significantly larger than a typical app bar).
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(20.dp)
                    .offset(x = backIconOffsetX),
            )
        }
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.width(54.dp))
    }
}
