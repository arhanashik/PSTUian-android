package com.workfort.pstuian.ui.signin.composable

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.workfort.pstuian.ui.signin.state.SignUpFormData

/**
 * Unified auth screen that hosts sign-in, sign-up, forgot-password, and email-verification panels.
 * Green header height follows panel state (hero vs compact app bar on Sign Up). The whole screen
 * scrolls so tall content and the software keyboard can move everything upward together.
 */
@Composable
fun SignInScreenUi(
    modifier: Modifier = Modifier,
    initialPanel: AuthPanel = AuthPanel.SignIn,
    onSkip: () -> Unit = {},
    onLogin: (email: String, password: String) -> Unit = { _, _ -> },
    onForgotPassword: (email: String) -> Unit = {},
    onEmailVerification: (email: String) -> Unit = {},
    onSignUp: (SignUpFormData) -> Unit = {},
) {
    var panel by remember { mutableStateOf(initialPanel) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    var registrationNumber by remember { mutableStateOf("") }
    var faculty by remember { mutableStateOf("") }
    var batch by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    // Green header height includes the status-bar safe area on edge-to-edge layouts.
    val statusBarInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
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
            onSkip = onSkip,
            onBackToSignIn = { panel = AuthPanel.SignIn },
        ) {
            AuthFormContent(
                panel = panel,
                email = email,
                password = password,
                name = name,
                studentId = studentId,
                registrationNumber = registrationNumber,
                faculty = faculty,
                batch = batch,
                rememberMe = rememberMe,
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onNameChange = { name = it },
                onStudentIdChange = { studentId = it },
                onRegistrationNumberChange = { registrationNumber = it },
                onFacultyChange = { faculty = it },
                onBatchChange = { batch = it },
                onRememberMeToggle = { rememberMe = !rememberMe },
                onLogin = { onLogin(email, password) },
                onForgotPasswordTap = { panel = AuthPanel.ForgotPassword },
                onEmailVerificationTap = { panel = AuthPanel.EmailVerification },
                onResetPasswordSubmit = { onForgotPassword(email) },
                onEmailVerificationSubmit = { onEmailVerification(email) },
                onSignUp = {
                    onSignUp(
                        SignUpFormData(
                            name = name,
                            email = email,
                            studentId = studentId,
                            registrationNumber = registrationNumber,
                            faculty = faculty,
                            batch = batch,
                            password = password,
                        ),
                    )
                },
                onSwitchToSignIn = { panel = AuthPanel.SignIn },
                onSwitchToSignUp = { panel = AuthPanel.SignUp },
            )
        }
    }
}
