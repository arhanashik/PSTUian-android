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
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.ui.common.composable.ActionButton

@Composable
internal fun ForgotPasswordAuthForm(
    email: String,
    onEmailChange: (String) -> Unit,
    onResetPasswordClicked: () -> Unit,
    onSwitchToSignIn: () -> Unit,
    bottomLinkPrefix: String = "Remember your password?",
    bottomLinkAction: String = "LOG IN",
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
                prefix = bottomLinkPrefix,
                action = bottomLinkAction,
                onAction = onSwitchToSignIn,
            )
        }
    }
}