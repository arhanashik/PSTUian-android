package com.workfort.pstuian.ui.notification.customnotification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.workfort.pstuian.ui.common.composable.dialog.ShowErrorDialog
import com.workfort.pstuian.ui.notification.customnotification.composable.CustomNotificationScreenContent
import com.workfort.pstuian.ui.notification.customnotification.state.CustomNotificationMessageState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CustomNotificationScreen(
    viewModel: CustomNotificationViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()

    CustomNotificationScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
}

@Composable
private fun HandleMessageState(
    message: CustomNotificationMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let { state ->
        when (state) {
            is CustomNotificationMessageState.ShowAlert -> {
                ShowErrorDialog(
                    title = state.title,
                    message = state.message,
                    cancelable = true,
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled,
                )
            }
            is CustomNotificationMessageState.Snackbar -> {
                ShowErrorDialog(
                    message = state.message,
                    cancelable = true,
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled,
                )
            }
        }
    }
}
