package com.workfort.pstuian.ui.students

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.workfort.pstuian.ui.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import com.workfort.pstuian.ui.common.theme.bgCircle
import com.workfort.pstuian.ui.students.composable.StudentsScreenContent
import com.workfort.pstuian.ui.students.state.StudentsMessageState
import com.workfort.pstuian.ui.students.state.StudentsNavigationState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_call
import pstuian.feature_presentation.generated.resources.txt_msg_call
import pstuian.feature_presentation.generated.resources.txt_title_call

@Composable
fun StudentsScreen(viewModel: StudentsViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    StudentsScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@Composable
private fun HandleMessageState(
    message: StudentsMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is StudentsMessageState.ConfirmCall -> {
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
    navigation: StudentsNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                is StudentsNavigationState.GoBack -> navigator?.goBack()
                is StudentsNavigationState.GoToStudentProfile -> {
                    navigator?.navigateTo(
                        AppScreen.Profile(
                            it.studentId,
                            com.workfort.pstuian.featuredomain.model.UserType.STUDENT
                        )
                    )
                }
            }
            onNavigationHandled()
        }
    }
}
