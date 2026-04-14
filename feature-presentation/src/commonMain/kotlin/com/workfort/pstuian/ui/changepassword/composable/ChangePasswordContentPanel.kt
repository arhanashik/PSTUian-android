package com.workfort.pstuian.ui.changepassword.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.common.composable.OutlinedTextInput
import com.workfort.pstuian.featuredomain.model.ChangePasswordInput
import com.workfort.pstuian.featuredomain.model.ChangePasswordInputError
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordUiEvent
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.hint_confirm_password
import pstuian.feature_presentation.generated.resources.hint_new_password
import pstuian.feature_presentation.generated.resources.hint_old_password
import pstuian.feature_presentation.generated.resources.txt_change

@Composable
internal fun ChangePasswordContentPanel(
    modifier: Modifier = Modifier,
    uiState: ChangePasswordUiState.Content,
    onUiEvent: (ChangePasswordUiEvent) -> Unit,
) {
    FormContent(
        modifier = modifier.fillMaxSize(),
        input = uiState.input,
        validationError = uiState.validationError,
        onUiEvent = onUiEvent,
    )
}

@Composable
private fun FormContent(
    modifier: Modifier = Modifier,
    input: ChangePasswordInput,
    validationError: ChangePasswordInputError,
    onUiEvent: (ChangePasswordUiEvent) -> Unit,
) {
    val (newInput, onChangeInput) = remember(input) { mutableStateOf(input) }
    var oldPasswordVisibility by remember { mutableStateOf(false) }
    var newPasswordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }

    LaunchedEffect(newInput) {
        onUiEvent(ChangePasswordUiEvent.InputChanged(newInput))
    }

    Column(modifier = modifier.padding(16.dp)) {
        OutlinedTextInput(
            label = stringResource(Res.string.hint_old_password),
            value = newInput.oldPassword,
            inputType = KeyboardType.Password,
            trailingIcon = {
                IconButton(onClick = { oldPasswordVisibility = !oldPasswordVisibility }) {
                    Icon(
                        imageVector = if (oldPasswordVisibility) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = if (oldPasswordVisibility) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (oldPasswordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            isError = validationError.oldPassword.isNotEmpty(),
            supportingText = validationError.oldPassword,
        ) {
            onChangeInput(newInput.copy(oldPassword = it))
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_new_password),
            value = newInput.newPassword,
            inputType = KeyboardType.Password,
            trailingIcon = {
                IconButton(onClick = { newPasswordVisibility = !newPasswordVisibility }) {
                    Icon(
                        imageVector = if (newPasswordVisibility) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = if (newPasswordVisibility) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (newPasswordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            isError = validationError.newPassword.isNotEmpty(),
            supportingText = validationError.newPassword,
        ) {
            onChangeInput(newInput.copy(newPassword = it))
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_confirm_password),
            value = newInput.confirmPassword,
            inputType = KeyboardType.Password,
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisibility = !confirmPasswordVisibility }) {
                    Icon(
                        imageVector = if (confirmPasswordVisibility) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = if (confirmPasswordVisibility) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (confirmPasswordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            isError = validationError.confirmPassword.isNotEmpty(),
            supportingText = validationError.confirmPassword,
        ) {
            onChangeInput(newInput.copy(confirmPassword = it))
        }
        TextButton(
            onClick = {
                onUiEvent(ChangePasswordUiEvent.ChangePasswordClicked)
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        ) {
            Text(
                stringResource(Res.string.txt_change),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}
