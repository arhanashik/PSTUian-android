package com.workfort.pstuian.ui.splash.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.delay

@Composable
internal fun MaintenanceTimer(remainingTime: Double) {
    var timeLeft by remember(remainingTime) {
        mutableStateOf(remainingTime)
    }

    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000L)
            timeLeft -= 1000L
        }
    }

    val totalSeconds = (timeLeft / 1000).toLong()
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds / 60) % 60
    val seconds = totalSeconds % 60

    val timeString = if (timeLeft > 0) {
        val h = hours.toString().padStart(2, '0')
        val m = minutes.toString().padStart(2, '0')
        val s = seconds.toString().padStart(2, '0')
        "$h:$m:$s"
    } else {
        "Finishing up..."
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Remaining Time",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = timeString,
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            ),
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
