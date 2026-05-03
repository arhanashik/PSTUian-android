package com.workfort.pstuian.ui.signin

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import com.workfort.pstuian.ui.common.composable.ListSelectionBottomSheet
import com.workfort.pstuian.ui.common.composable.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.ShowSuccessDialog
import com.workfort.pstuian.ui.common.composable.batchesToListSelectionOptions
import com.workfort.pstuian.ui.common.composable.facultiesToListSelectionOptions
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.signin.composable.SignInScreenContent
import com.workfort.pstuian.ui.signin.state.SignInMessageState
import com.workfort.pstuian.ui.signin.state.SignInNavigationState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.btn_select
import pstuian.feature_presentation.generated.resources.txt_select_batch
import pstuian.feature_presentation.generated.resources.txt_select_faculty

@Composable
fun SignInScreen(viewModel: SignInViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    SignInScreenContent(uiState = uiState, onUiEvent = viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HandleMessageState(
    message: SignInMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is SignInMessageState.Success -> {
                ShowSuccessDialog(message = it.message, onConfirm = onMessageHandled)
            }
            is SignInMessageState.Error -> {
                ShowErrorDialog(
                    message = it.message,
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled,
                )
            }
            is SignInMessageState.FacultySelection -> {
                ListSelectionBottomSheet(
                    title = stringResource(Res.string.txt_select_faculty),
                    primaryButtonLabel = stringResource(Res.string.btn_select),
                    options = facultiesToListSelectionOptions(it.faculties),
                    initialSelection = it.faculties.find { f -> f.id == it.selectedFacultyId },
                    scrollable = true,
                    onDismiss = onMessageHandled,
                    onConfirm = { faculty -> it.onSaveAndContinue(faculty) },
                )
            }
            is SignInMessageState.BatchSelection -> {
                ListSelectionBottomSheet(
                    title = stringResource(Res.string.txt_select_batch),
                    primaryButtonLabel = stringResource(Res.string.btn_select),
                    options = batchesToListSelectionOptions(it.batches),
                    initialSelection = it.batches.find { b -> b.id == it.selectedBatchId },
                    scrollable = true,
                    onDismiss = onMessageHandled,
                    onConfirm = { batch -> it.onSaveAndContinue(batch) },
                )
            }

            else -> {}
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: SignInNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(key1 = navigation) {
        navigation?.let {
            when (it) {
                is SignInNavigationState.GoBack -> navigator?.goBack()
                is SignInNavigationState.OpenWebScreen -> uriHandler.openUri(it.url)
            }
            onNavigationHandled()
        }
    }
}
