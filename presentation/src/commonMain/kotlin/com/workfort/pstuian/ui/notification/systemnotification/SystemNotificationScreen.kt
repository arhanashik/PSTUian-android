package com.workfort.pstuian.ui.notification.systemnotification

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.workfort.pstuian.ui.notification.systemnotification.composable.SystemNotificationScreenContent
import com.workfort.pstuian.ui.notification.systemnotification.state.SystemNotificationMessageState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SystemNotificationScreen(
    viewModel: SystemNotificationViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()

    SystemNotificationScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
}

@Composable
private fun HandleMessageState(
    message: SystemNotificationMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let { state ->
        when (state) {
            is SystemNotificationMessageState.ShowDetail -> {
                AlertDialog(
                    onDismissRequest = onMessageHandled,
                    title = { Text(text = state.notification.title) },
                    text = { Text(text = state.notification.body) },
                    confirmButton = {
                        TextButton(onClick = onMessageHandled) {
                            Text("OK")
                        }
                    },
                )
            }
        }
    }
}
