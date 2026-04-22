package com.workfort.pstuian.ui.signin.composable

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.workfort.pstuian.ui.common.composable.ShowLoaderDialog
import com.workfort.pstuian.ui.signin.screendata.AuthPanel
import com.workfort.pstuian.ui.signin.state.SignInUiEvent
import com.workfort.pstuian.ui.signin.state.SignInUiState

@Composable
fun SignInContentPanel(
    uiState: SignInUiState,
    onUiEvent: (SignInUiEvent) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val panel = when (uiState) {
            is SignInUiState.None, is SignInUiState.SignInPanel -> AuthPanel.SignIn
            is SignInUiState.StudentSignUpPanel -> AuthPanel.SignUp
            is SignInUiState.ForgotPasswordPanel -> AuthPanel.ForgotPassword
            is SignInUiState.EmailVerificationPanel -> AuthPanel.EmailVerification
        }

        // Green header height includes the status-bar safe area on edge-to-edge layouts.
        val statusBarInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val totalHeight = maxHeight
            val compactGreenHeight = CompactHeaderHeight + statusBarInset
            val targetGreenHeight = when (panel) {
                AuthPanel.SignUp -> compactGreenHeight
                AuthPanel.SignIn -> totalHeight * SignInGreenHeightFraction
                else -> totalHeight * ExpandedGreenHeightFraction
            }

            val greenHeight by animateDpAsState(
                targetValue = targetGreenHeight,
                animationSpec = tween(
                    durationMillis = SectionResizeDurationMillis,
                    easing = SectionResizeEasing,
                ),
                label = "greenSectionHeight",
            )

            SignInAuthForeground(
                greenHeight = greenHeight,
                panel = panel,
                onSkip = { onUiEvent(SignInUiEvent.BackClicked) },
                onBackToSignIn = { onUiEvent(SignInUiEvent.AuthPanelChanged(AuthPanel.SignIn)) },
            ) {
                AuthFormForSignInUiState(uiState, onUiEvent)
            }
        }

        if (uiState.isLoading) {
            ShowLoaderDialog()
        }
    }
}
