package com.workfort.pstuian.ui.signin.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.composable.ActionButton
import com.workfort.pstuian.ui.common.theme.TextStyle

@Composable
internal fun EmailVerificationAuthForm(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSendVerificationClicked: () -> Unit,
    onSwitchToSignIn: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val emailFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }

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
            AuthPasswordField(
                password = password,
                onPasswordChange = { onPasswordChange(it) },
                focusRequester = passwordFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "A verification link will be sent to your inbox",
                style = TextStyle.label2.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
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