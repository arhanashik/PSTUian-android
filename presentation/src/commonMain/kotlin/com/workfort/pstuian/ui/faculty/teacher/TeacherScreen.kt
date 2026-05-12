package com.workfort.pstuian.ui.faculty.teacher

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.composable.dialog.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.theme.bgCircle
import com.workfort.pstuian.ui.faculty.teacher.composable.TeacherScreenContent
import com.workfort.pstuian.ui.faculty.teacher.state.TeacherMessageState
import com.workfort.pstuian.ui.faculty.teacher.state.TeacherNavigationState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import pstuian.presentation.generated.resources.Res
import pstuian.presentation.generated.resources.txt_call
import pstuian.presentation.generated.resources.txt_msg_call
import pstuian.presentation.generated.resources.txt_title_call

@Composable
fun TeacherScreen(
    facultyId: Int,
    viewModel: TeacherViewModel = koinViewModel { parametersOf(facultyId) },
) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    TeacherScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@Composable
private fun HandleMessageState(
    message: TeacherMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is TeacherMessageState.ConfirmCall -> {
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
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: TeacherNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                is TeacherNavigationState.GoToTeacherProfile -> {
                    navigator?.navigateToProfile(it.userId, UserType.TEACHER)
                }
            }
            onNavigationHandled()
        }
    }
}
