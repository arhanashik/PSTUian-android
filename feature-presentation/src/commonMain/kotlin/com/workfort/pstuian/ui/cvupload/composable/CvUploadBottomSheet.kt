package com.workfort.pstuian.ui.cvupload.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.composable.dialog.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.dialog.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.dialog.ShowLoaderDialog
import com.workfort.pstuian.ui.common.composable.TitleTextSmall
import com.workfort.pstuian.ui.cvupload.CvUploadViewModel
import com.workfort.pstuian.ui.cvupload.state.CvUploadMessageState
import com.workfort.pstuian.ui.cvupload.state.CvUploadUiState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.label_upload_cv_screen
import pstuian.feature_presentation.generated.resources.txt_dismiss
import pstuian.feature_presentation.generated.resources.txt_upload

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CvUploadBottomSheet(
    userId: Int,
    userType: UserType,
    onDismiss: (isSuccess: Boolean) -> Unit,
    viewModel: CvUploadViewModel = koinViewModel { parametersOf(userId, userType) },
) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewModel.onUiReady()
    }

    LaunchedEffect(Unit) {
        viewModel.finishSuccess.collect {
            sheetState.hide()
            onDismiss(true)
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss(false) },
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp, bottom = 24.dp),
        ) {
            TitleTextSmall(text = stringResource(Res.string.label_upload_cv_screen))
            when (val state = uiState) {
                is CvUploadUiState.None -> Unit
                is CvUploadUiState.Content ->
                    CvUploadContentPanel(
                        modifier = Modifier.padding(top = 8.dp),
                        uiState = state,
                        onUiEvent = viewModel::onUiEvent,
                    )
            }
        }
    }

    HandleMessageState(message = message, onMessageHandled = viewModel::onMessageHandled)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HandleMessageState(
    message: CvUploadMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is CvUploadMessageState.ConfirmUpload -> {
                ShowConfirmationDialog(
                    message = it.message,
                    confirmButtonText = stringResource(Res.string.txt_upload),
                    dismissButtonText = stringResource(Res.string.txt_dismiss),
                    onConfirm = {
                        onMessageHandled()
                        it.onConfirm()
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is CvUploadMessageState.Loading -> {
                ShowLoaderDialog(cancelable = it.cancelable)
            }
            is CvUploadMessageState.Error -> {
                ShowErrorDialog(
                    message = it.message,
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled,
                )
            }
        }
    }
}
