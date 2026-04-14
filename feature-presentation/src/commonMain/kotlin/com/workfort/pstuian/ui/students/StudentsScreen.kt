package com.workfort.pstuian.ui.students

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.workfort.pstuian.common.composable.AppBar
import com.workfort.pstuian.common.composable.AppScaffold
import com.workfort.pstuian.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.common.navigation.AppNavigator
import com.workfort.pstuian.common.navigation.AppScreen
import com.workfort.pstuian.common.theme.bgCircle
import com.workfort.pstuian.ui.students.composable.StudentsContentPanel
import com.workfort.pstuian.ui.students.state.StudentsMessageState
import com.workfort.pstuian.ui.students.state.StudentsNavigationState
import com.workfort.pstuian.ui.students.state.StudentsUiEvent
import com.workfort.pstuian.ui.students.state.StudentsUiState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_call
import pstuian.feature_presentation.generated.resources.txt_msg_call
import pstuian.feature_presentation.generated.resources.txt_title_call

@Composable
internal fun StudentsScreen(viewModel: StudentsViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    StudentsScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationConsumed)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StudentsScreenContent(
    uiState: StudentsUiState,
    onUiEvent: (StudentsUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    AppScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                title = uiState.title,
                navigation = { onUiEvent(StudentsUiEvent.BackClicked) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
        StudentsContentPanel(uiState, onUiEvent)
    }
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
