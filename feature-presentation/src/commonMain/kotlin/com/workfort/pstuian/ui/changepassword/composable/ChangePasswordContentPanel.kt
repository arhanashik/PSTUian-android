package com.workfort.pstuian.ui.changepassword.composable

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordInput
import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordInputError
import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordScreenPanel
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordUiEvent
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordUiState
import com.workfort.pstuian.ui.common.composable.ActionButton
import com.workfort.pstuian.ui.signin.composable.AuthBottomLink
import com.workfort.pstuian.ui.signin.composable.AuthFormPanelLayout
import com.workfort.pstuian.ui.signin.composable.AuthUnderlinedField
import com.workfort.pstuian.ui.signin.composable.ForgotPasswordAuthForm
import com.workfort.pstuian.ui.signin.screendata.AuthFormFieldSpacing
import com.workfort.pstuian.ui.signin.screendata.ExpandedGreenHeightFraction
import com.workfort.pstuian.ui.signin.screendata.SectionResizeDurationMillis
import com.workfort.pstuian.ui.signin.screendata.SectionResizeEasing
import com.workfort.pstuian.ui.signin.screendata.SignInGreenHeightFraction
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.hint_confirm_password
import pstuian.feature_presentation.generated.resources.hint_new_password
import pstuian.feature_presentation.generated.resources.hint_old_password

@Composable
internal fun ChangePasswordContentPanel(
    uiState: ChangePasswordUiState.Content,
    changePasswordHeaderTitle: String,
    onUiEvent: (ChangePasswordUiEvent) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val panel = uiState.activePanel

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val totalHeight = maxHeight
            val targetGreenHeight = when (panel) {
                ChangePasswordScreenPanel.ChangePassword -> totalHeight * SignInGreenHeightFraction
                ChangePasswordScreenPanel.ResetPassword -> totalHeight * ExpandedGreenHeightFraction
            }

            val greenHeight by animateDpAsState(
                targetValue = targetGreenHeight,
                animationSpec = tween(
                    durationMillis = SectionResizeDurationMillis,
                    easing = SectionResizeEasing,
                ),
                label = "changePasswordGreenHeight",
            )

            ChangePasswordAuthForeground(
                greenHeight = greenHeight,
                panel = panel,
                changePasswordHeaderTitle = changePasswordHeaderTitle,
                onBackFromChangePanel = { onUiEvent(ChangePasswordUiEvent.BackClicked) },
                onBackFromResetPanel = {
                    onUiEvent(ChangePasswordUiEvent.PanelChanged(ChangePasswordScreenPanel.ChangePassword))
                },
            ) {
                when (panel) {
                    ChangePasswordScreenPanel.ChangePassword -> ChangePasswordAuthForm(
                        input = uiState.input,
                        validationError = uiState.validationError,
                        onUiEvent = onUiEvent,
                    )
                    ChangePasswordScreenPanel.ResetPassword -> ForgotPasswordAuthForm(
                        email = uiState.resetEmail,
                        onEmailChange = { onUiEvent(ChangePasswordUiEvent.ResetEmailChanged(it)) },
                        onResetPasswordClicked = {
                            onUiEvent(ChangePasswordUiEvent.SendPasswordResetClicked)
                        },
                        onSwitchToSignIn = {
                            onUiEvent(ChangePasswordUiEvent.PanelChanged(ChangePasswordScreenPanel.ChangePassword))
                        },
                        bottomLinkPrefix = "Remembered old password?",
                        bottomLinkAction = "Change",
                    )
                }
            }
        }
    }
}

@Composable
private fun ChangePasswordAuthForm(
    input: ChangePasswordInput,
    validationError: ChangePasswordInputError,
    onUiEvent: (ChangePasswordUiEvent) -> Unit,
) {
    val (newInput, onChangeInput) = remember(input) { mutableStateOf(input) }
    val focusManager = LocalFocusManager.current
    val oldFocus = remember { FocusRequester() }
    val newFocus = remember { FocusRequester() }
    val confirmFocus = remember { FocusRequester() }

    LaunchedEffect(newInput) {
        onUiEvent(ChangePasswordUiEvent.InputChanged(newInput))
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        AuthFormPanelLayout {
            ChangePasswordPasswordField(
                label = stringResource(Res.string.hint_old_password),
                value = newInput.oldPassword,
                onValueChange = { onChangeInput(newInput.copy(oldPassword = it)) },
                focusRequester = oldFocus,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(onNext = { newFocus.requestFocus() }),
                isError = validationError.oldPassword.isNotEmpty(),
                supportingText = validationError.oldPassword.takeIf { it.isNotEmpty() },
            )
            Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
            ChangePasswordPasswordField(
                label = stringResource(Res.string.hint_new_password),
                value = newInput.newPassword,
                onValueChange = { onChangeInput(newInput.copy(newPassword = it)) },
                focusRequester = newFocus,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(onNext = { confirmFocus.requestFocus() }),
                isError = validationError.newPassword.isNotEmpty(),
                supportingText = validationError.newPassword.takeIf { it.isNotEmpty() },
            )
            Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
            ChangePasswordPasswordField(
                label = stringResource(Res.string.hint_confirm_password),
                value = newInput.confirmPassword,
                onValueChange = { onChangeInput(newInput.copy(confirmPassword = it)) },
                focusRequester = confirmFocus,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                isError = validationError.confirmPassword.isNotEmpty(),
                supportingText = validationError.confirmPassword.takeIf { it.isNotEmpty() },
            )
            Spacer(modifier = Modifier.height(24.dp))
            ActionButton(label = "UPDATE PASSWORD", icon = Icons.AutoMirrored.Filled.ArrowForward) {
                onUiEvent(ChangePasswordUiEvent.ChangePasswordClicked)
            }
            Spacer(modifier = Modifier.height(16.dp))
            AuthBottomLink(
                prefix = "Forgot password?",
                action = "Reset",
                onAction = {
                    onUiEvent(ChangePasswordUiEvent.PanelChanged(ChangePasswordScreenPanel.ResetPassword))
                },
            )
        }
    }
}

@Composable
private fun ChangePasswordPasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    isError: Boolean = false,
    supportingText: String? = null,
) {
    var visible by remember { mutableStateOf(false) }
    AuthUnderlinedField(
        label = label,
        value = value,
        onValueChange = onValueChange,
        focusRequester = focusRequester,
        visualTransformation = if (visible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        isError = isError,
        supportingText = supportingText,
        trailingContent = {
            Icon(
                imageVector = if (visible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                modifier = Modifier
                    .size(20.dp)
                    .clickable { visible = !visible },
            )
        },
    )
}
