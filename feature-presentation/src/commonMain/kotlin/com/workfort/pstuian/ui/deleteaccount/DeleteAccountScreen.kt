package com.workfort.pstuian.app.ui.common.ui.deleteaccount

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.workfort.pstuian.reducer.ui.deleteaccount.DeleteAccountScreenState
import com.workfort.pstuian.common.component.AppBar
import com.workfort.pstuian.common.component.OutlinedTextInput
import com.workfort.pstuian.common.component.ShowConfirmationDialog
import com.workfort.pstuian.common.component.ShowErrorDialog
import com.workfort.pstuian.common.component.ShowLoaderDialog
import com.workfort.pstuian.common.component.ShowSuccessDialog
import com.workfort.pstuian.common.component.TitleTextSmall
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.hint_password
import pstuian.feature_presentation.generated.resources.msg_delete_account
import pstuian.feature_presentation.generated.resources.txt_delete_account


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountScreen(
    modifier: Modifier = Modifier,
    screenState: DeleteAccountScreenState,
    onUiEvent: (DeleteAccountUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = stringResource(Res.string.txt_delete_account),
                onClickBack = {
                    onUiEvent(DeleteAccountUiEvent.OnClickBack)
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
    input: String,
    validationError: String,
    onUiEvent: (DeleteAccountUiEvent) -> Unit,
) {
    val (newInput, onChangeInput) = remember(input) { mutableStateOf(input) }
    var passwordVisibility by remember { mutableStateOf(false) }

    LaunchedEffect(newInput) {
        onUiEvent(DeleteAccountUiEvent.OnChangeInput(newInput))
    }

    Column(modifier = modifier.padding(16.dp)) {
        TitleTextSmall(text = stringResource(Res.string.msg_delete_account))
        Spacer(modifier = Modifier.padding(top = 16.dp))
        OutlinedTextInput(
            label = stringResource(Res.string.hint_password),
            value = newInput,
            inputType = KeyboardType.Password,
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisibility) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (passwordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            isError = validationError.isNotEmpty(),
            supportingText = validationError,
        ) {
            onChangeInput(it)
        }
        TextButton(
            onClick = {
                onUiEvent(DeleteAccountUiEvent.OnClickDeleteAccountBtn)
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            enabled = validationError.isEmpty(),
        ) {
            Text(
                stringResource(Res.string.txt_delete_account),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}

@Composable
private fun DeleteAccountScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (DeleteAccountUiEvent) -> Unit,
) {
    when (this) {
        is DeleteAccountScreenState.DisplayState.MessageState.Loading -> {
            ShowLoaderDialog(cancelable = cancelable)
        }
        is DeleteAccountScreenState.DisplayState.MessageState.ConfirmAccountDelete -> {
            ShowConfirmationDialog(
                title = stringResource(Res.string.txt_delete_account),
                message = stringResource(Res.string.msg_delete_account),
                onConfirm = {
                    onUiEvent(DeleteAccountUiEvent.OnDeleteAccount)
                },
                onDismiss = {
                    onUiEvent(DeleteAccountUiEvent.MessageConsumed)
                }
            )
        }
        is DeleteAccountScreenState.DisplayState.MessageState.Success -> {
            ShowSuccessDialog(
                message = message,
                cancelable = false,
                confirmButtonText = "Request Recovery",
                dismissButtonText = "Open Home Screen",
                onConfirm = {
                    onUiEvent(DeleteAccountUiEvent.OnRequestRecovery)
                },
                onDismiss = {
                    onUiEvent(DeleteAccountUiEvent.OnResetToHomeScreen)
                }
            )
        }
        is DeleteAccountScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                message = message,
                onConfirm = {
                    onUiEvent(DeleteAccountUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(DeleteAccountUiEvent.MessageConsumed)
                }
            )
        }
    }
}