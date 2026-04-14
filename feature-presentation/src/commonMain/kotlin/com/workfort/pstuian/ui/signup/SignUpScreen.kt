package com.workfort.pstuian.ui.signup

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
import com.workfort.pstuian.common.composable.NavigationButton
import com.workfort.pstuian.common.composable.ShowErrorDialog
import com.workfort.pstuian.common.composable.ShowSuccessDialog
import com.workfort.pstuian.common.navigation.AppNavigator
import com.workfort.pstuian.common.navigation.AppScreen
import com.workfort.pstuian.ui.signup.composable.SignUpContentPanel
import com.workfort.pstuian.ui.signup.state.SignUpMessageState
import com.workfort.pstuian.ui.signup.state.SignUpNavigationState
import com.workfort.pstuian.ui.signup.state.SignUpUiEvent
import com.workfort.pstuian.ui.signup.state.SignUpUiState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.success_msg_sign_up
import pstuian.feature_presentation.generated.resources.txt_sign_in
import pstuian.feature_presentation.generated.resources.txt_sign_up

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    facultyId: Int?,
    batchId: Int?,
    onOpenUrl: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    LaunchedEffect(key1 = facultyId, key2 = batchId) {
        if (facultyId != null) {
            if (batchId == null) {
                viewModel.onEvent(SignUpUiEvent.FacultyChanged(facultyId))
            } else {
                viewModel.onEvent(SignUpUiEvent.BatchChanged(batchId))
            }
        }
    }

    SignUpScreenContent(
        uiState = uiState,
        onUiEvent = { event ->
            when (event) {
                is SignUpUiEvent.TermsAndConditionsClicked ->
                    onOpenUrl("https://pstuian.com/terms-and-conditions")
                is SignUpUiEvent.PrivacyPolicyClicked ->
                    onOpenUrl("https://pstuian.com/privacy-policy")
                else -> viewModel.onEvent(event)
            }
        },
    )

    HandleMessageState(
        message = message,
        onUiEvent = viewModel::onEvent,
        onMessageHandled = viewModel::onMessageHandled,
    )

    HandleNavigationState(
        navigation = navigation,
        onNavigationHandled = viewModel::onNavigationConsumed,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignUpScreenContent(
    uiState: SignUpUiState,
    onUiEvent: (SignUpUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    AppScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                title = stringResource(Res.string.txt_sign_up),
                navigation = {
                    NavigationButton {
                        onUiEvent(SignUpUiEvent.BackClicked)
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
        SignUpContentPanel(uiState, onUiEvent)
    }
}

@Composable
private fun HandleMessageState(
    message: SignUpMessageState?,
    onUiEvent: (SignUpUiEvent) -> Unit,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is SignUpMessageState.SignUpSuccess -> {
                ShowSuccessDialog(
                    message = stringResource(Res.string.success_msg_sign_up),
                    confirmButtonText = stringResource(Res.string.txt_sign_in),
                    cancelable = false,
                    onConfirm = {
                        onMessageHandled()
                        onUiEvent(SignUpUiEvent.BackClicked)
                    }
                )
            }
            is SignUpMessageState.Error -> {
                ShowErrorDialog(
                    message = it.message,
                    onConfirm = {
                        onMessageHandled()
                    },
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
    navigation: SignUpNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                is SignUpNavigationState.GoBack -> {
                    navigator?.goBack()
                }
                is SignUpNavigationState.GoToFacultyPickerScreen -> {
                    navigator?.navigateTo(
                        AppScreen.FacultyPicker(
                            it.mode,
                            it.facultyId,
                            it.batchId
                        )
                    )
                }
            }
            onNavigationHandled()
        }
    }
}
