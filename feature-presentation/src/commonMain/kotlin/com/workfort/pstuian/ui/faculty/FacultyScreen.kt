package com.workfort.pstuian.ui.faculty

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.composable.dialog.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.dialog.ShowErrorDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import com.workfort.pstuian.ui.common.theme.bgCircle
import com.workfort.pstuian.ui.faculty.composable.FacultyScreenContent
import com.workfort.pstuian.ui.faculty.state.FacultyMessageState
import com.workfort.pstuian.ui.faculty.state.FacultyNavigationState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_call
import pstuian.feature_presentation.generated.resources.txt_msg_call
import pstuian.feature_presentation.generated.resources.txt_title_call

@Composable
fun FacultyScreen(viewModel: FacultyViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    FacultyScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationConsumed)
}

@Composable
private fun HandleMessageState(
    message: FacultyMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is FacultyMessageState.ConfirmCall -> {
                ShowConfirmationDialog(
                    icon = Icons.Default.Call,
                    iconModifier = Modifier.bgCircle(),
                    title = stringResource(Res.string.txt_title_call),
                    message = stringResource(Res.string.txt_msg_call).plus(" ${it.phoneNumber}"),
                    confirmButtonText = stringResource(Res.string.txt_call),
                    onConfirm = {
                        it.onConfirm()
                        onMessageHandled()
                    },
                    onDismiss = { onMessageHandled() },
                )
            }
            is FacultyMessageState.ShowError -> {
                ShowErrorDialog(
                    title = it.title,
                    message = it.message,
                    onConfirm = it.onRetry,
                    onDismiss = onMessageHandled,
                )
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: FacultyNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                is FacultyNavigationState.GoBack -> navigator?.goBack()
                is FacultyNavigationState.GoToStudentsScreen -> {
                    navigator?.navigateTo(AppScreen.Students(it.batchId))
                }
                is FacultyNavigationState.GoToTeacherProfileScreen -> {
                    navigator?.navigateToProfile(it.userId, UserType.TEACHER)
                }
                is FacultyNavigationState.GoToEmployeeProfileScreen -> {
                    navigator?.navigateToProfile(it.userId, UserType.EMPLOYEE)
                }
            }
            onNavigationHandled()
        }
    }
}
