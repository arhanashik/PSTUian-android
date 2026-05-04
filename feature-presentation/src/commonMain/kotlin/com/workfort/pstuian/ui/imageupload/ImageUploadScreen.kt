package com.workfort.pstuian.ui.imageupload

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.AppSnackbarHost
import com.workfort.pstuian.ui.common.composable.HandleSnackbar
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.ShowLoaderDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.imageupload.composable.ImageUploadContentPanel
import com.workfort.pstuian.ui.imageupload.state.ImageUploadMessageState
import com.workfort.pstuian.ui.imageupload.state.ImageUploadNavigationState
import com.workfort.pstuian.ui.imageupload.state.ImageUploadUiEvent
import com.workfort.pstuian.ui.imageupload.state.ImageUploadUiState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.msg_upload_profile_image
import pstuian.feature_presentation.generated.resources.txt_dismiss
import pstuian.feature_presentation.generated.resources.txt_upload

@Composable
fun ImageUploadScreen(viewModel: ImageUploadViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    ScreenContent(uiState, snackbarHostState, onUiEvent = viewModel::onUiEvent)

    HandleMessageState(message, snackbarHostState, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    uiState: ImageUploadUiState,
    snackbarHostState: SnackbarHostState,
    onUiEvent: (ImageUploadUiEvent) -> Unit,
) {
    AppScaffold(
        topBar = {
            AppBar(
                title = "Upload Image",
                navigation = {
                    NavigationButton { onUiEvent(ImageUploadUiEvent.BackClicked) }
                },
            )
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) }
    ) {
        when (uiState) {
            is ImageUploadUiState.None -> Unit
            is ImageUploadUiState.Content -> {
                ImageUploadContentPanel(uiState = uiState, onUiEvent = onUiEvent)
            }
        }
    }
}

@Composable
private fun HandleMessageState(
    message: ImageUploadMessageState?,
    snackbarHostState: SnackbarHostState,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is ImageUploadMessageState.ConfirmUpload -> {
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
            is ImageUploadMessageState.Loading -> {
                ShowLoaderDialog(cancelable = it.cancelable)
            }
            is ImageUploadMessageState.Error -> {
                ShowErrorDialog(
                    message = it.message,
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled,
                )
            }
            is ImageUploadMessageState.Snackbar -> {
                HandleSnackbar(it.message, snackbarHostState, onMessageHandled)
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: ImageUploadNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                is ImageUploadNavigationState.GoBack -> {
                    navigator?.goBack()
                }
            }
            onNavigationHandled()
        }
    }
}
