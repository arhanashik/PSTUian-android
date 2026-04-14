package com.workfort.pstuian.ui.cvupload

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.workfort.pstuian.common.composable.AppBar
import com.workfort.pstuian.common.composable.AppScaffold
import com.workfort.pstuian.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.common.composable.ShowErrorDialog
import com.workfort.pstuian.common.composable.ShowSuccessDialog
import com.workfort.pstuian.common.navigation.AppNavigator
import com.workfort.pstuian.ui.cvupload.composable.CvUploadContentPanel
import com.workfort.pstuian.ui.cvupload.state.CvUploadMessageState
import com.workfort.pstuian.ui.cvupload.state.CvUploadNavigationState
import com.workfort.pstuian.ui.cvupload.state.CvUploadUiEvent
import com.workfort.pstuian.ui.cvupload.state.CvUploadUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.msg_upload_new_cv
import pstuian.feature_presentation.generated.resources.txt_dismiss
import pstuian.feature_presentation.generated.resources.txt_upload

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CvUploadScreen(
    viewModel: CvUploadViewModel,
    navigator: AppNavigator,
) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onUiReady()
    }

    LaunchedEffect(navigation) {
        when (navigation) {
            is CvUploadNavigationState.GoBack -> {
                navigator.goBack()
                viewModel.onNavigationHandled()
            }
            null -> Unit
        }
    }

    message?.let {
        when (it) {
            is CvUploadMessageState.ConfirmUpload -> {
                ShowConfirmationDialog(
                    message = stringResource(Res.string.msg_upload_new_cv),
                    confirmButtonText = stringResource(Res.string.txt_upload),
                    dismissButtonText = stringResource(Res.string.txt_dismiss),
                    onConfirm = {
                        viewModel.onMessageHandled()
                        // This should trigger the actual upload in the UI layer (platform specific)
                        // For now we assume some mechanism exists or will be added.
                    },
                    onDismiss = viewModel::onMessageHandled,
                )
            }
            is CvUploadMessageState.Success -> {
                ShowSuccessDialog(
                    message = it.message,
                    onConfirm = viewModel::onMessageHandled,
                    onDismiss = viewModel::onMessageHandled,
                )
            }
            is CvUploadMessageState.Error -> {
                ShowErrorDialog(
                    message = it.message,
                    onConfirm = viewModel::onMessageHandled,
                    onDismiss = viewModel::onMessageHandled,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CvUploadScreenContent(
    uiState: CvUploadUiState,
    onUiEvent: (CvUploadUiEvent) -> Unit,
) {
    AppScaffold (
        topBar = {
            AppBar(
                title = "Upload CV",
                navigation = { onUiEvent(CvUploadUiEvent.OnClickBack) },
            )
        },
    ) { innerPadding ->
        when (uiState) {
            is CvUploadUiState.None -> Unit
            is CvUploadUiState.Content -> {
                CvUploadContentPanel(
                    modifier = Modifier.padding(innerPadding),
                    uiState = uiState,
                    onUiEvent = onUiEvent,
                )
            }
        }
    }
}
