package com.workfort.pstuian.ui.signin

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.common.composable.AppBar
import com.workfort.pstuian.ui.signin.composable.SignInContentPanel
import com.workfort.pstuian.ui.signin.state.NavigationState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_sign_in

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel,
    onNavigateBack: (Boolean) -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToEmailVerification: () -> Unit,
    showToast: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = uiState.navigationState) {
        when (val state = uiState.navigationState) {
            is NavigationState.GoBack -> onNavigateBack(state.isSignedIn)
            is NavigationState.GoToForgotPasswordScreen -> onNavigateToForgotPassword()
            is NavigationState.GoToSignUpScreen -> onNavigateToSignUp()
            is NavigationState.GoToEmailVerificationScreen -> onNavigateToEmailVerification()
            null -> Unit
        }
        if (uiState.navigationState != null) {
            viewModel.navigationConsumed()
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = stringResource(Res.string.txt_sign_in),
                onClickBack = {
                    viewModel.onEvent(SignInUiEvent.OnClickBack)
                },
                elevation = 0.dp,
            )
        },
    ) { innerPadding ->
        SignInContentPanel(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState,
            messageState = uiState.messageState,
            onShowToast = showToast,
            onUiEvent = viewModel::onEvent,
        )
    }
}
