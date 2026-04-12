package com.workfort.pstuian.view.ui.changepassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.reducer.ui.changepassword.ChangePasswordScreenState
import com.workfort.pstuian.model.ChangePasswordInput
import com.workfort.pstuian.model.ChangePasswordInputError
import com.workfort.pstuian.view.ui.common.component.AppBar
import com.workfort.pstuian.view.ui.common.component.OutlinedTextInput
import com.workfort.pstuian.view.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.view.ui.common.component.ShowLoaderDialog
import com.workfort.pstuian.view.ui.common.component.ShowSuccessDialog
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pstuian.shared.generated.resources.Res
import pstuian.shared.generated.resources.hint_confirm_password
import pstuian.shared.generated.resources.hint_new_password
import pstuian.shared.generated.resources.hint_old_password
import pstuian.shared.generated.resources.txt_change
import pstuian.shared.generated.resources.txt_change_password


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    modifier: Modifier = Modifier,
    screenState: ChangePasswordScreenState,
    onUiEvent: (ChangePasswordUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = stringResource(Res.string.txt_change_password),
                onClickBack = {
                    onUiEvent(ChangePasswordUiEvent.OnClickBack)
                },
                elevation = 0.dp,
            )
        },
    ) { innerPadding ->
        with(screenState.displayState) {
            FormContent(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                input = input,
                validationError = validationError,
                onUiEvent = onUiEvent,
            )
            messageState?.Handle(onUiEvent)
        }
    }
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
        onUiEvent(ChangePasswordUiEvent.OnChangeInput(newInput))
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
                onUiEvent(ChangePasswordUiEvent.OnClickSaveBtn)
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

@Composable
private fun ChangePasswordScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (ChangePasswordUiEvent) -> Unit,
) {
    when (this) {
        is ChangePasswordScreenState.DisplayState.MessageState.Loading -> {
            ShowLoaderDialog(cancelable = cancelable)
        }
        is ChangePasswordScreenState.DisplayState.MessageState.Success -> {
            ShowSuccessDialog(
                message = message,
                onConfirm = {
                    onUiEvent(ChangePasswordUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(ChangePasswordUiEvent.MessageConsumed)
                }
            )
        }
        is ChangePasswordScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                message = message,
                onConfirm = {
                    onUiEvent(ChangePasswordUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(ChangePasswordUiEvent.MessageConsumed)
                }
            )
        }
    }
}