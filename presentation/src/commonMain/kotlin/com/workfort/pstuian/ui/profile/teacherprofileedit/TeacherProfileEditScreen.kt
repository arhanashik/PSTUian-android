package com.workfort.pstuian.ui.profile.teacherprofileedit

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
import com.workfort.pstuian.ui.common.composable.bottomsheet.facultiesToListSelectionOptions
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.profile.teacherprofileedit.composable.TeacherProfileScreenContent
import com.workfort.pstuian.ui.profile.teacherprofileedit.state.TeacherProfileEditMessageState
import com.workfort.pstuian.ui.profile.teacherprofileedit.state.TeacherProfileEditNavigationState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.presentation.generated.resources.Res
import pstuian.presentation.generated.resources.btn_select
import pstuian.presentation.generated.resources.msg_confirm_save_profile
import pstuian.presentation.generated.resources.txt_select_faculty

@Composable
fun TeacherProfileEditScreen(viewModel: TeacherProfileEditViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    TeacherProfileScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onUiEvent = viewModel::onUiEvent,
    )

    HandleMessageState(message, snackbarHostState, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@Composable
private fun HandleMessageState(
    message: TeacherProfileEditMessageState?,
    snackbarHostState: SnackbarHostState,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is TeacherProfileEditMessageState.Loading -> {
                ShowLoaderDialog(cancelable = it.cancelable)
            }
            is TeacherProfileEditMessageState.FacultySelection -> {
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
            is TeacherProfileEditMessageState.ConfirmSave -> {
                ShowConfirmationDialog(
                    message = stringResource(Res.string.msg_confirm_save_profile),
                    onConfirm = {
                        onMessageHandled()
                        it.onConfirm()
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is TeacherProfileEditMessageState.ShowSnackBar -> {
                HandleSnackbar(it.message, snackbarHostState, onMessageHandled)
            }
            is TeacherProfileEditMessageState.Error -> {
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
    navigation: TeacherProfileEditNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                is TeacherProfileEditNavigationState.GoBack -> {
                    navigator.goBack()
                }
            }
            onNavigationHandled()
        }
    }
}
