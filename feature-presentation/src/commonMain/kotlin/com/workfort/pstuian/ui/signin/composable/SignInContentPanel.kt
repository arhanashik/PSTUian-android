package com.workfort.pstuian.ui.signin.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.workfort.pstuian.ui.common.composable.ShowLoaderDialog
import com.workfort.pstuian.ui.signin.state.SignInUiEvent
import com.workfort.pstuian.ui.signin.state.SignInUiState

@Composable
fun SignInContentPanel(
    uiState: SignInUiState,
    onUiEvent: (SignInUiEvent) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        SignInScreenUi(
            onSkip = { onUiEvent(SignInUiEvent.BackClicked) },
            onLogin = { email, password ->
                onUiEvent(SignInUiEvent.SignInClicked(email, password))
            },
            onForgotPassword = { email ->
                onUiEvent(SignInUiEvent.ForgotPasswordSubmitted(email))
            },
            onSignUp = { data ->
                onUiEvent(SignInUiEvent.SignUpSubmitted(data))
            },
        )

        if (uiState.isLoading) {
            ShowLoaderDialog()
        }
    }
}
