package com.workfort.pstuian.ui.teacherprofileedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.composable.AppBar
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.AppSnackbarHost
import com.workfort.pstuian.ui.common.composable.NavigationButton
import com.workfort.pstuian.ui.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.ShowLoaderDialog
import com.workfort.pstuian.ui.common.composable.ShowSuccessDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import com.workfort.pstuian.ui.teacherprofileedit.composable.TeacherProfileEditContentPanel
import com.workfort.pstuian.ui.teacherprofileedit.state.TeacherProfileEditMessageState
import com.workfort.pstuian.ui.teacherprofileedit.state.TeacherProfileEditNavigationState
import com.workfort.pstuian.ui.teacherprofileedit.state.TeacherProfileEditUiEvent
import com.workfort.pstuian.ui.teacherprofileedit.state.TeacherProfileEditUiState
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_edit
import pstuian.feature_presentation.generated.resources.txt_save_changes

@Composable
fun TeacherProfileEditScreen(viewModel: TeacherProfileEditViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    ScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onUiEvent = viewModel::onUiEvent,
    )

    HandleMessageState(message, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    uiState: TeacherProfileEditUiState,
    snackbarHostState: SnackbarHostState,
    onUiEvent: (TeacherProfileEditUiEvent) -> Unit,
) {
    var fabButtonExpanded by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = null) {
        delay(1000)
        fabButtonExpanded = false
    }

    AppScaffold(
        topBar = {
            AppBar(
                title = stringResource(Res.string.txt_edit),
                navigation = {
                    NavigationButton { onUiEvent(TeacherProfileEditUiEvent.ClickBack) }
                },
            )
        },
        floatingActionButton = {
            if (uiState is TeacherProfileEditUiState.Content) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    ExtendedFloatingActionButton(
                        expanded = fabButtonExpanded,
                        text = { Text(text = stringResource(Res.string.txt_save_changes)) },
                        onClick = {
                            onUiEvent(TeacherProfileEditUiEvent.ClickSave)
                        },
                        icon = { Icon(Icons.Outlined.CheckCircle, "") },
                        shape = CircleShape,
                    )
                }
            }
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        when (uiState) {
            is TeacherProfileEditUiState.None -> Unit
            is TeacherProfileEditUiState.Content -> {
                TeacherProfileEditContentPanel(
                    modifier = Modifier.padding(innerPadding),
                    uiState = uiState,
                    onUiEvent = onUiEvent,
                )
            }
        }
    }
}

@Composable
private fun HandleMessageState(
    message: TeacherProfileEditMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is TeacherProfileEditMessageState.Loading -> {
                ShowLoaderDialog(cancelable = it.cancelable)
            }
            is TeacherProfileEditMessageState.ConfirmSave -> {
                ShowConfirmationDialog(
                    message = "Are you surely want to save the changes?",
                    onConfirm = {
                        onMessageHandled()
                        it.onConfirm()
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is TeacherProfileEditMessageState.Success -> {
                ShowSuccessDialog(
                    message = it.message,
                    confirmButtonText = "Go Back",
                    dismissButtonText = "Edit More",
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled,
                )
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
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(key1 = navigation) {
        navigation?.let {
            when (it) {
                is TeacherProfileEditNavigationState.GoBack -> {
                    navigator?.goBack()
                }
                is TeacherProfileEditNavigationState.GoToFacultyPickerScreen -> {
                    navigator?.navigateTo(
                        AppScreen.FacultyPicker(
                            mode = it.mode,
                            facultyId = it.facultyId,
                            batchId = null,
                        )
                    )
                }
            }
            onNavigationHandled()
        }
    }
}
