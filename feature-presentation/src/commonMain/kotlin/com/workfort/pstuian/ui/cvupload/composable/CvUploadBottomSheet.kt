package com.workfort.pstuian.ui.cvupload.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.composable.AppSnackbarHost
import com.workfort.pstuian.ui.common.composable.HandleSnackbar
import com.workfort.pstuian.ui.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.ShowLoaderDialog
import com.workfort.pstuian.ui.common.composable.TitleTextSmall
import com.workfort.pstuian.ui.cvupload.CvUploadViewModel
import com.workfort.pstuian.ui.cvupload.state.CvUploadMessageState
import com.workfort.pstuian.ui.cvupload.state.CvUploadNavigationState
import com.workfort.pstuian.ui.cvupload.state.CvUploadUiEvent
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
    onDismiss: () -> Unit,
    viewModel: CvUploadViewModel = koinViewModel { parametersOf(userId, userType) },
) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        viewModel.onUiReady()
    }

    LaunchedEffect(navigation) {
        when (navigation) {
            is CvUploadNavigationState.GoBack -> {
                sheetState.hide()
                viewModel.onNavigationHandled()
                onDismiss()
            }
            null -> Unit
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 8.dp, bottom = 24.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TitleTextSmall(text = stringResource(Res.string.label_upload_cv_screen))
                    IconButton(
                        onClick = { viewModel.onUiEvent(CvUploadUiEvent.BackClicked) },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(Res.string.txt_dismiss),
                        )
                    }
                }
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
            AppSnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
            )
        }
    }

    CvUploadSheetMessageEffects(
        message = message,
        snackbarHostState = snackbarHostState,
        onMessageHandled = viewModel::onMessageHandled,
    )
}

@Composable
private fun CvUploadSheetMessageEffects(
    message: CvUploadMessageState?,
    snackbarHostState: SnackbarHostState,
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
            is CvUploadMessageState.Snackbar -> {
                HandleSnackbar(it.message, snackbarHostState, onMessageHandled)
            }
        }
    }
}
