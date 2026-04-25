package com.workfort.pstuian.ui.profile.studentprofileedit

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
import com.workfort.pstuian.ui.common.composable.ShowInfoDialog
import com.workfort.pstuian.ui.common.composable.ShowLoaderDialog
import com.workfort.pstuian.ui.common.composable.ShowSuccessDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import com.workfort.pstuian.ui.profile.studentprofileedit.composable.StudentProfileEditContentPanel
import com.workfort.pstuian.ui.profile.studentprofileedit.state.StudentProfileEditMessageState
import com.workfort.pstuian.ui.profile.studentprofileedit.state.StudentProfileEditNavigationState
import com.workfort.pstuian.ui.profile.studentprofileedit.state.StudentProfileEditUiEvent
import com.workfort.pstuian.ui.profile.studentprofileedit.state.StudentProfileEditUiState
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_edit
import pstuian.feature_presentation.generated.resources.txt_save_changes

@Composable
fun StudentProfileEditScreen(viewModel: StudentProfileEditViewModel) {
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
    uiState: StudentProfileEditUiState,
    snackbarHostState: SnackbarHostState,
    onUiEvent: (StudentProfileEditUiEvent) -> Unit,
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
                    NavigationButton { onUiEvent(StudentProfileEditUiEvent.ClickBack) }
                },
            )
        },
        floatingActionButton = {
            if (uiState is StudentProfileEditUiState.Content) {
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
                            onUiEvent(StudentProfileEditUiEvent.ClickSave)
                        },
                        icon = { Icon(Icons.Outlined.CheckCircle, "") },
                        shape = CircleShape,
                    )
                }
            }
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) }
    ) {
        when (uiState) {
            is StudentProfileEditUiState.None -> Unit
            is StudentProfileEditUiState.Content -> {
                StudentProfileEditContentPanel(uiState, onUiEvent)
            }
        }
    }
}

@Composable
private fun HandleMessageState(
    message: StudentProfileEditMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is StudentProfileEditMessageState.Loading -> {
                ShowLoaderDialog(cancelable = it.cancelable)
            }
            is StudentProfileEditMessageState.ConfirmSave -> {
                ShowConfirmationDialog(
                    message = "Are you surely want to save the changes?",
                    onConfirm = {
                        onMessageHandled()
                        it.onConfirm()
                    },
                    onDismiss = {
                        onMessageHandled()
                    }
                )
            }
            is StudentProfileEditMessageState.Success -> {
                ShowSuccessDialog(
                    message = it.message,
                    confirmButtonText = "Go Back",
                    dismissButtonText = "Edit More",
                    onConfirm = {
                        onMessageHandled()
                    },
                    onDismiss = {
                        onMessageHandled()
                    }
                )
            }
            is StudentProfileEditMessageState.Error -> {
                ShowInfoDialog(
                    message = it.message,
                    onDismiss = {
                        onMessageHandled()
                    }
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

    LaunchedEffect(key1 = navigation) {
        navigation?.let {
            when (it) {
                is StudentProfileEditNavigationState.GoBack -> {
                    navigator.goBack()
                }
                is StudentProfileEditNavigationState.GoToFacultyPickerScreen -> {
                    navigator.navigateTo(
                        AppScreen.FacultyPicker(
                            mode = it.mode,
                            facultyId = it.facultyId,
                            batchId = it.batchId,
                        )
                    )
                }
            }
            onNavigationHandled()
        }
    }
}
