package com.workfort.pstuian.ui.signin.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.ui.common.composable.ActionButton
import com.workfort.pstuian.ui.signin.screendata.AuthPanel
import com.workfort.pstuian.ui.signin.screendata.SignInFormData
import com.workfort.pstuian.ui.signin.screendata.SignUpFormData
import com.workfort.pstuian.ui.signin.state.SignInUiEvent
import com.workfort.pstuian.ui.signin.state.SignInUiState

private val AuthFormFieldSpacing = 18.dp

@Composable
internal fun AuthFormForSignInUiState(
    uiState: SignInUiState,
    onUiEvent: (SignInUiEvent) -> Unit,
) {
    when (uiState) {
        is SignInUiState.None -> Unit
        is SignInUiState.SignInPanel -> SignInAuthForm(uiState.formData, uiState.rememberMe, onUiEvent)
        is SignInUiState.SignUpPanel -> SignUpAuthForm(uiState.formData, onUiEvent)
        is SignInUiState.ForgotPasswordPanel -> {
            ForgotPasswordAuthForm(
                email = uiState.email,
                onEmailChange = { onUiEvent(SignInUiEvent.EmailChanged(it)) },
                onResetPasswordClicked = { onUiEvent(SignInUiEvent.ForgotPasswordClicked(uiState.email)) },
                onSwitchToSignIn = { onUiEvent(SignInUiEvent.AuthPanelChanged(AuthPanel.SignIn)) },
            )
        }
        is SignInUiState.EmailVerificationPanel -> {
            EmailVerificationAuthForm(
                email = uiState.email,
                onEmailChange = { onUiEvent(SignInUiEvent.EmailChanged(it)) },
                onSendVerificationClicked = { onUiEvent(SignInUiEvent.EmailVerificationClicked(uiState.email)) },
                onSwitchToSignIn = { onUiEvent(SignInUiEvent.AuthPanelChanged(AuthPanel.SignIn)) },
            )
        }
    }
}

@Composable
internal fun SignInAuthForm(
    formData: SignInFormData,
    rememberMe: Boolean,
    onUiEvent: (SignInUiEvent) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val emailFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }

    Column(modifier = Modifier.fillMaxWidth()) {
        AuthFormPanelLayout {
            AuthUnderlinedField(
                label = "Email Address",
                value = formData.email,
                onValueChange = { onUiEvent(SignInUiEvent.EmailChanged(it)) },
                focusRequester = emailFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
            )
            Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
            AuthPasswordField(
                password = formData.password,
                onPasswordChange = { onUiEvent(SignInUiEvent.PasswordChanged(it)) },
                focusRequester = passwordFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            )
            Spacer(modifier = Modifier.height(16.dp))
            RememberMeRow(
                rememberMe = rememberMe,
                onRememberMeToggle = { onUiEvent(SignInUiEvent.SignInRememberMeToggled(!rememberMe)) },
                onForgotPassword = { onUiEvent(SignInUiEvent.AuthPanelChanged(AuthPanel.ForgotPassword)) },
            )
            Spacer(modifier = Modifier.height(24.dp))
            AuthBottomLink(
                prefix = "Need to verify your email?",
                action = "Verify",
                onAction = { onUiEvent(SignInUiEvent.AuthPanelChanged(AuthPanel.EmailVerification)) },
            )
            Spacer(modifier = Modifier.height(24.dp))
            ActionButton("LOGIN", Icons.AutoMirrored.Filled.ArrowForward) {
                onUiEvent(SignInUiEvent.SignInClicked(formData))
            }
            Spacer(modifier = Modifier.height(24.dp))
            AuthBottomLink(
                prefix = "Don't have an account?",
                action = "SIGN UP",
                onAction = { onUiEvent(SignInUiEvent.AuthPanelChanged(AuthPanel.SignUp)) },
            )
        }
    }
}

@Composable
internal fun SignUpAuthForm(
    formData: SignUpFormData,
    onUiEvent: (SignInUiEvent) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val nameFocus = remember { FocusRequester() }
    val emailFocus = remember { FocusRequester() }
    val studentIdFocus = remember { FocusRequester() }
    val registrationFocus = remember { FocusRequester() }
    val facultyFocus = remember { FocusRequester() }
    val batchFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }

    Column(modifier = Modifier.fillMaxWidth()) {
        AuthFormPanelLayout {
            AuthUnderlinedField(
                label = "Name",
                value = formData.name,
                onValueChange = { onUiEvent(SignInUiEvent.SignUpFormDataChanged(formData.copy(name = it))) },
                focusRequester = nameFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { emailFocus.requestFocus() }),
            )
            Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
            AuthUnderlinedField(
                label = "Email Address",
                value = formData.email,
                onValueChange = { onUiEvent(SignInUiEvent.EmailChanged(it)) },
                focusRequester = emailFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { studentIdFocus.requestFocus() }),
            )
            Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
            AuthUnderlinedField(
                label = "Student Id",
                value = formData.studentId,
                onValueChange = { onUiEvent(SignInUiEvent.SignUpFormDataChanged(formData.copy(studentId = it))) },
                focusRequester = studentIdFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { registrationFocus.requestFocus() }),
            )
            Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
            AuthUnderlinedField(
                label = "Registration Number",
                value = formData.regNumber,
                onValueChange = { onUiEvent(SignInUiEvent.SignUpFormDataChanged(formData.copy(regNumber = it))) },
                focusRequester = registrationFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { facultyFocus.requestFocus() }),
            )
            Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {
                AuthUnderlinedField(
                    label = "Faculty",
                    value = formData.faculty,
                    onValueChange = { onUiEvent(SignInUiEvent.SignUpFormDataChanged(formData.copy(faculty = it))) },
                    modifier = Modifier.weight(1f),
                    focusRequester = facultyFocus,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { batchFocus.requestFocus() }),
                )
                Spacer(modifier = Modifier.width(16.dp))
                AuthUnderlinedField(
                    label = "Batch",
                    value = formData.batch,
                    onValueChange = { onUiEvent(SignInUiEvent.SignUpFormDataChanged(formData.copy(batch = it))) },
                    modifier = Modifier.weight(1f),
                    focusRequester = batchFocus,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
                )
            }
            Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
            AuthPasswordField(
                password = formData.password,
                onPasswordChange = { onUiEvent(SignInUiEvent.PasswordChanged(it)) },
                focusRequester = passwordFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            )
            Spacer(modifier = Modifier.height(24.dp))
            AuthPrivacyPolicyAndTermsLink(
                onTermsAndConditionsClick = { onUiEvent(SignInUiEvent.TermsAndConditionsClicked) },
                onPrivacyPolicyClick = { onUiEvent(SignInUiEvent.PrivacyPolicyClicked) },
            )
            Spacer(modifier = Modifier.height(24.dp))
            ActionButton("SIGN UP", Icons.AutoMirrored.Filled.ArrowForward) {
                onUiEvent(SignInUiEvent.SignUpClicked(formData))
            }
            Spacer(modifier = Modifier.height(24.dp))
            AuthBottomLink(
                prefix = "Already have an account?",
                action = "LOG IN",
                onAction = { onUiEvent(SignInUiEvent.AuthPanelChanged(AuthPanel.SignIn)) },
            )
        }
    }
}

@Composable
internal fun ForgotPasswordAuthForm(
    email: String,
    onEmailChange: (String) -> Unit,
    onResetPasswordClicked: () -> Unit,
    onSwitchToSignIn: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val emailFocus = remember { FocusRequester() }

    Column(modifier = Modifier.fillMaxWidth()) {
        AuthFormPanelLayout {
            AuthUnderlinedField(
                label = "Email Address",
                value = email,
                onValueChange = onEmailChange,
                focusRequester = emailFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Enter your email and we'll send you a link to reset your password.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp,
            )
            Spacer(modifier = Modifier.height(24.dp))
            ActionButton("RESET PASSWORD", Icons.AutoMirrored.Filled.ArrowForward) {
                onResetPasswordClicked()
            }
            Spacer(modifier = Modifier.height(24.dp))
            AuthBottomLink(
                prefix = "Remember your password?",
                action = "LOG IN",
                onAction = onSwitchToSignIn,
            )
        }
    }
}

@Composable
internal fun EmailVerificationAuthForm(
    email: String,
    onEmailChange: (String) -> Unit,
    onSendVerificationClicked: () -> Unit,
    onSwitchToSignIn: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val emailFocus = remember { FocusRequester() }

    Column(modifier = Modifier.fillMaxWidth()) {
        AuthFormPanelLayout {
            AuthUnderlinedField(
                label = "Email Address",
                value = email,
                onValueChange = onEmailChange,
                focusRequester = emailFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Enter your email and we'll send you a verification link to confirm your account.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp,
            )
            Spacer(modifier = Modifier.height(24.dp))
            ActionButton("SEND VERIFICATION EMAIL", Icons.AutoMirrored.Filled.ArrowForward) {
                onSendVerificationClicked()
            }
            Spacer(modifier = Modifier.height(24.dp))
            AuthBottomLink(
                prefix = "Already verified?",
                action = "LOG IN",
                onAction = onSwitchToSignIn,
            )
        }
    }
}
