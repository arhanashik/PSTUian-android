package com.workfort.pstuian.ui.common.composable.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.Notification
import com.workfort.pstuian.ui.common.theme.TextStyle

@Composable
fun NotificationAlertDialog(
    notification: Notification.SystemNotification,
    showDontShowAgain: Boolean,
    onDismiss: (Boolean) -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    var dontShowAgain by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { }, // Don't dismiss the dialog by clicking outside
        title = { Text(text = notification.title) },
        text = {
            Column {
                Text(text = notification.body)
                if (showDontShowAgain) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = dontShowAgain,
                            onCheckedChange = { dontShowAgain = it }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Don't show again", style = TextStyle.label2)
                    }
                }
            }
        },
        confirmButton = {
            notification.link?.let { link ->
                Button(
                    onClick = {
                        runCatching { uriHandler.openUri(link) }
                        onDismiss(dontShowAgain)
                    }
                ) {
                    Text(text = notification.linkText ?: "Open")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss(dontShowAgain) }) {
                Text("Dismiss")
            }
        },
    )
}
