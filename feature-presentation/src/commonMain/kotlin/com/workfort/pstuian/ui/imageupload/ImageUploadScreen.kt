package com.workfort.pstuian.ui.imageupload

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import coil3.compose.LocalPlatformContext
import com.workfort.pstuian.ui.common.composable.HandleSnackbar
import com.workfort.pstuian.ui.common.composable.dialog.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.dialog.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.dialog.ShowLoaderDialog
import com.workfort.pstuian.ui.common.composable.clearSingletonCoilImageCaches
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.imageupload.composable.ImageUploadScreenContent
import com.workfort.pstuian.ui.imageupload.state.ImageUploadMessageState
import com.workfort.pstuian.ui.imageupload.state.ImageUploadNavigationState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_dismiss
import pstuian.feature_presentation.generated.resources.txt_upload

@Composable
fun ImageUploadScreen(viewModel: ImageUploadViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    ImageUploadScreenContent(uiState, snackbarHostState, onUiEvent = viewModel::onUiEvent)

    HandleMessageState(message, snackbarHostState, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
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
    val platformContext = LocalPlatformContext.current

    LaunchedEffect(navigation, platformContext) {
        navigation?.let {
            when (it) {
                is ImageUploadNavigationState.GoBack -> {
                    if (it.invalidateImageCache) {
                        clearSingletonCoilImageCaches(platformContext)
                    }
                    navigator?.goBack()
                }
            }
            onNavigationHandled()
        }
    }
}
