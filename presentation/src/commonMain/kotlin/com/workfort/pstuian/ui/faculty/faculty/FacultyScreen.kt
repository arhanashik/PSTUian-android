package com.workfort.pstuian.ui.faculty.faculty

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.workfort.pstuian.ui.common.composable.dialog.ShowErrorDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.faculty.faculty.composable.FacultyScreenContent
import com.workfort.pstuian.ui.faculty.faculty.state.FacultyMessageState
import com.workfort.pstuian.ui.faculty.faculty.state.FacultyNavigationState
import org.koin.compose.koinInject

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
            is FacultyMessageState.ShowError -> {
                ShowErrorDialog(
                    title = it.title,
                    message = it.message,
                    cancelable = false,
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
            }
            onNavigationHandled()
        }
    }
}
