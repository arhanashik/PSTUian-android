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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.composable.ShowLoaderDialog
import com.workfort.pstuian.ui.signin.screendata.AuthPanel
import com.workfort.pstuian.ui.signin.screendata.CompactHeaderHeight
import com.workfort.pstuian.ui.signin.screendata.ExpandedGreenHeightFraction
import com.workfort.pstuian.ui.signin.screendata.SectionResizeDurationMillis
import com.workfort.pstuian.ui.signin.screendata.SectionResizeEasing
import com.workfort.pstuian.ui.signin.screendata.SignInGreenHeightFraction
import com.workfort.pstuian.ui.signin.screendata.SignUpFormData
import com.workfort.pstuian.ui.signin.screendata.SignUpToggleResizeDurationMillis
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
            is SignInUiState.SignUpPanel -> when (uiState.formData) {
                is SignUpFormData.StudentSignUpFormData -> AuthPanel.StudentSignUp
                is SignUpFormData.TeacherSignUpFormData -> AuthPanel.TeacherSignUp
            }
            is SignInUiState.ForgotPasswordPanel -> AuthPanel.ForgotPassword
            is SignInUiState.EmailVerificationPanel -> AuthPanel.EmailVerification
        }
        var previousPanel by remember { mutableStateOf(panel) }
        val isSignUpTypeToggle = previousPanel.isSignUpPanel() && panel.isSignUpPanel()
        LaunchedEffect(panel) {
            previousPanel = panel
        }

        // Green header height includes the status-bar safe area on edge-to-edge layouts.
        val statusBarInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val totalHeight = maxHeight
            val compactGreenHeight = CompactHeaderHeight + statusBarInset
            val targetGreenHeight = when (panel) {
                AuthPanel.StudentSignUp, AuthPanel.TeacherSignUp -> compactGreenHeight
                AuthPanel.SignIn -> totalHeight * SignInGreenHeightFraction
                else -> totalHeight * ExpandedGreenHeightFraction
            }

            val greenHeight by animateDpAsState(
                targetValue = targetGreenHeight,
                animationSpec = tween(
                    durationMillis = if (isSignUpTypeToggle) {
                        SignUpToggleResizeDurationMillis
                    } else {
                        SectionResizeDurationMillis
                    },
                    easing = SectionResizeEasing,
                ),
                label = "greenSectionHeight",
            )

            val showStudentTeacherToggle =
                uiState is SignInUiState.SignInPanel || uiState is SignInUiState.SignUpPanel
            val authUserTypeForForms = when (uiState) {
                is SignInUiState.SignInPanel -> uiState.authUserTypeForForms
                is SignInUiState.SignUpPanel -> uiState.authUserTypeForForms
                else -> UserType.STUDENT
            }
            SignInAuthForeground(
                greenHeight = greenHeight,
                panel = panel,
                showStudentTeacherToggle = showStudentTeacherToggle,
                authUserTypeForForms = authUserTypeForForms,
                onAuthUserTypeForFormsChange = { onUiEvent(SignInUiEvent.AuthUserTypeForFormsToggled(it)) },
                onSkip = { onUiEvent(SignInUiEvent.BackClicked) },
                onBackToSignIn = { onUiEvent(SignInUiEvent.AuthPanelChanged(AuthPanel.SignIn)) },
            ) {
                when (uiState) {
                    is SignInUiState.None -> Unit
                    is SignInUiState.SignInPanel -> SignInAuthForm(
                        formData = uiState.formData,
                        onUiEvent = onUiEvent,
                    )
                    is SignInUiState.SignUpPanel -> SignUpAuthFormContent(
                        formData = uiState.formData,
                        onUiEvent = onUiEvent,
                    )
                    is SignInUiState.ForgotPasswordPanel -> {
                        ForgotPasswordAuthForm(
                            email = uiState.email,
                            validationError = "",
                            onEmailChange = { onUiEvent(SignInUiEvent.ForgotPasswordFormDataChanged(email = it)) },
                            onResetPasswordClicked = { onUiEvent(SignInUiEvent.ForgotPasswordClicked(uiState.email)) },
                            onSwitchToSignIn = { onUiEvent(SignInUiEvent.AuthPanelChanged(AuthPanel.SignIn)) },
                        )
                    }
                    is SignInUiState.EmailVerificationPanel -> {
                        EmailVerificationAuthForm(
                            formData = uiState.formData,
                            onFormDataChanged = {
                                onUiEvent(SignInUiEvent.EmailVerificationFormDataChanged(formData = it))
                            },
                            onSendVerificationClicked = {
                                onUiEvent(SignInUiEvent.EmailVerificationClicked(uiState.formData))
                            },
                            onSwitchToSignIn = { onUiEvent(SignInUiEvent.AuthPanelChanged(AuthPanel.SignIn)) },
                        )
                    }
                }
            }
        }

        if (uiState.isLoading) {
            ShowLoaderDialog()
        }
    }
}

private fun AuthPanel.isSignUpPanel(): Boolean {
    return this == AuthPanel.StudentSignUp || this == AuthPanel.TeacherSignUp
}
