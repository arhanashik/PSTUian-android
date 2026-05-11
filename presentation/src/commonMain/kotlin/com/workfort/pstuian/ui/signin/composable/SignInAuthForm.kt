package com.workfort.pstuian.ui.signin.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.composable.ActionButton
import com.workfort.pstuian.ui.signin.screendata.AuthFormFieldSpacing
import com.workfort.pstuian.ui.signin.screendata.AuthPanel
import com.workfort.pstuian.ui.signin.screendata.SignInFormData
import com.workfort.pstuian.ui.signin.state.SignInUiEvent

@Composable
internal fun SignInAuthForm(
    formData: SignInFormData,
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
                onValueChange = { onUiEvent(SignInUiEvent.SignInFormDataChanged(formData.copy(email = it))) },
                focusRequester = emailFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
            )
            Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
            AuthPasswordField(
                password = formData.password,
                onPasswordChange = { onUiEvent(SignInUiEvent.SignInFormDataChanged(formData.copy(password = it))) },
                focusRequester = passwordFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            )
            Spacer(modifier = Modifier.height(16.dp))
            RememberMeRow(
                rememberMe = formData.rememberMe,
                onRememberMeToggle = {
                    onUiEvent(SignInUiEvent.SignInFormDataChanged(formData.copy(rememberMe = !formData.rememberMe)))
                },
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
                onAction = { onUiEvent(SignInUiEvent.SignUpFromSignInClicked) },
            )
        }
    }
}
