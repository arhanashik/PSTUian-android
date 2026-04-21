package com.workfort.pstuian.ui.signin.composable

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.workfort.pstuian.ui.signin.state.SignUpFormData

/**
 * Unified auth screen that hosts sign-in, sign-up, forgot-password, and email-verification panels.
 * Switching between panels animates the green header between the tall hero section and a compact
 * app bar, while the email field stays in place and the remaining fields animate in or out per
 * panel.
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

    val density = LocalDensity.current

    // On iOS the screen is drawn edge-to-edge (see [SignInScreenContent]) so the green and white
    // sections can color the status bar and home indicator areas respectively. These insets let us
    // size each section to include its safe area while still keeping the inner interactive content
    // padded out of the system bars.
    val statusBarInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navigationBarInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val totalHeight = maxHeight
        var formContentHeight by remember(totalHeight) {
            mutableStateOf(totalHeight * 0.42f)
        }

        val preferredWhite = formContentHeight + WhiteSectionVerticalBuffer + navigationBarInset
        val compactGreenHeight = CompactHeaderHeight + statusBarInset
        val targetGreenHeight = if (preferredWhite > totalHeight * MAX_WHITE_FRACTION) {
            compactGreenHeight
        } else {
            totalHeight - preferredWhite
        }

        val greenHeight by animateDpAsState(
            targetValue = targetGreenHeight,
            animationSpec = tween(
                durationMillis = SectionResizeDurationMillis,
                easing = SectionResizeEasing,
            ),
            label = "greenSectionHeight",
        )

        SignInAuthBackdrop(greenHeight = greenHeight)

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
                onContentMeasured = { heightPx ->
                    formContentHeight = with(density) { heightPx.toDp() }
                },
            )
        }
    }
}
