package com.workfort.pstuian.ui.profile.studentprofileedit

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.workfort.pstuian.ui.common.composable.HandleSnackbar
import com.workfort.pstuian.ui.common.composable.bottomsheet.ListSelectionBottomSheet
import com.workfort.pstuian.ui.common.composable.dialog.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.dialog.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.dialog.ShowLoaderDialog
import com.workfort.pstuian.ui.common.composable.bottomsheet.batchesToListSelectionOptions
import com.workfort.pstuian.ui.common.composable.bottomsheet.facultiesToListSelectionOptions
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.profile.studentprofileedit.composable.StudentProfileScreenContent
import com.workfort.pstuian.ui.profile.studentprofileedit.state.StudentProfileEditMessageState
import com.workfort.pstuian.ui.profile.studentprofileedit.state.StudentProfileEditNavigationState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.btn_select
import pstuian.feature_presentation.generated.resources.msg_confirm_save_profile
import pstuian.feature_presentation.generated.resources.txt_select_batch
import pstuian.feature_presentation.generated.resources.txt_select_faculty

@Composable
fun StudentProfileEditScreen(viewModel: StudentProfileEditViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    StudentProfileScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onUiEvent = viewModel::onUiEvent,
    )

    HandleMessageState(message, snackbarHostState, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@Composable
private fun HandleMessageState(
    message: StudentProfileEditMessageState?,
    snackbarHostState: SnackbarHostState,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is StudentProfileEditMessageState.Loading -> {
                ShowLoaderDialog(cancelable = it.cancelable)
            }
            is StudentProfileEditMessageState.FacultySelection -> {
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
            is StudentProfileEditMessageState.BatchSelection -> {
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
            is StudentProfileEditMessageState.ConfirmSave -> {
                ShowConfirmationDialog(
                    message = stringResource(Res.string.msg_confirm_save_profile),
                    onConfirm = {
                        onMessageHandled()
                        it.onConfirm()
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is StudentProfileEditMessageState.ShowSnackBar -> {
                HandleSnackbar(it.message, snackbarHostState, onMessageHandled)
            }
            is StudentProfileEditMessageState.Error -> {
                ShowErrorDialog(
                    message = it.message,
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled,
                )
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: StudentProfileEditNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                is StudentProfileEditNavigationState.GoBack -> {
                    navigator.goBack()
                }
            }
            onNavigationHandled()
        }
    }
}
