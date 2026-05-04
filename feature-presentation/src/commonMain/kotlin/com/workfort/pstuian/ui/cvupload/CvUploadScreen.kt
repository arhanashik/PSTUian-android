package com.workfort.pstuian.ui.cvupload

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.workfort.pstuian.ui.common.composable.HandleSnackbar
import com.workfort.pstuian.ui.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.ShowErrorDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.cvupload.composable.CvUploadScreenContent
import com.workfort.pstuian.ui.cvupload.state.CvUploadMessageState
import com.workfort.pstuian.ui.cvupload.state.CvUploadNavigationState
import com.workfort.pstuian.ui.cvupload.state.CvUploadUiEvent
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.msg_upload_new_cv
import pstuian.feature_presentation.generated.resources.txt_dismiss
import pstuian.feature_presentation.generated.resources.txt_upload

@Composable
fun CvUploadScreen(viewModel: CvUploadViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    CvUploadScreenContent(uiState, snackbarHostState, onUiEvent = viewModel::onUiEvent)

    HandleMessageState(message, snackbarHostState, viewModel::onMessageHandled, viewModel::onUiEvent)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@Composable
private fun HandleMessageState(
    message: CvUploadMessageState?,
    snackbarHostState: SnackbarHostState,
    onMessageHandled: () -> Unit,
    onUiEvent: (CvUploadUiEvent) -> Unit,
) {
    message?.let {
        when (it) {
            is CvUploadMessageState.ConfirmUpload -> {
                ShowConfirmationDialog(
                    message = stringResource(Res.string.msg_upload_new_cv),
                    confirmButtonText = stringResource(Res.string.txt_upload),
                    dismissButtonText = stringResource(Res.string.txt_dismiss),
                    onConfirm = {
                        onMessageHandled()
                        onUiEvent(CvUploadUiEvent.ConfirmUpload)
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is CvUploadMessageState.Error -> {
                ShowErrorDialog(
                    message = it.message,
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled,
                )
            }
            is CvUploadMessageState.Snackbar -> {
                HandleSnackbar(it.message, snackbarHostState, onMessageHandled)
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: CvUploadNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                is CvUploadNavigationState.GoBack -> {
                    navigator?.goBack()
                }
            }
            onNavigationHandled()
        }
    }
}
