package com.workfort.pstuian.app.ui.common.ui.forgotpassword

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.reducer.ui.forgotpassword.ForgotPasswordScreenState
import com.workfort.pstuian.common.component.AppBar
import com.workfort.pstuian.common.component.HorizontalDividerWithLabel
import com.workfort.pstuian.common.component.MaterialButtonToggleGroup
import com.workfort.pstuian.common.component.OutlinedTextInput
import com.workfort.pstuian.common.component.ShowErrorDialog
import com.workfort.pstuian.common.component.ShowLoaderDialog
import com.workfort.pstuian.common.component.ShowSuccessDialog
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.hint_email
import pstuian.feature_presentation.generated.resources.hint_password_recovery
import pstuian.feature_presentation.generated.resources.hint_remembered_password
import pstuian.feature_presentation.generated.resources.label_forgot_password_screen
import pstuian.feature_presentation.generated.resources.txt_or
import pstuian.feature_presentation.generated.resources.txt_send_reset_link
import pstuian.feature_presentation.generated.resources.txt_sign_in
import pstuian.feature_presentation.generated.resources.txt_user_types


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    screenState: ForgotPasswordScreenState,
    onUiEvent: (ForgotPasswordScreenUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = stringResource(Res.string.label_forgot_password_screen),
                onClickBack = {
                    onUiEvent(ForgotPasswordScreenUiEvent.OnClickBack)
                },
                elevation = 0.dp,
            )
        },
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            Column(modifier = modifier.padding(horizontal = 16.dp)) {
                ForgotPasswordFormContent(modifier, screenState.displayState, onUiEvent)
                ForgotPasswordFooterContent(modifier, onUiEvent)
            }
            if (screenState.displayState.isLoading) {
                ShowLoaderDialog()
            }
        }
    }

    screenState.displayState.messageState?.let { messageState ->
        when (messageState) {
            is ForgotPasswordScreenState.DisplayState.MessageState.PasswordResetLinkSentSuccess -> {
                ShowSuccessDialog(
                    message = messageState.message,
                    confirmButtonText = stringResource(Res.string.txt_sign_in),
                    cancelable = false,
                    onConfirm = {
                        onUiEvent(ForgotPasswordScreenUiEvent.MessageConsumed)
                        onUiEvent(ForgotPasswordScreenUiEvent.OnClickBack)
                    }
                )
            }
            is ForgotPasswordScreenState.DisplayState.MessageState.Error -> {
                ShowErrorDialog(
                    message = messageState.message,
                    dismissButtonText = null,
                    onConfirm = {
                        onUiEvent(ForgotPasswordScreenUiEvent.MessageConsumed)
                    },
                    onDismiss = { /* NO OP */ }
                )
            }
        }
    }
}

@Composable
private fun ForgotPasswordFormContent(
    modifier: Modifier = Modifier,
    displayState: ForgotPasswordScreenState.DisplayState,
    onUiEvent: (ForgotPasswordScreenUiEvent) -> Unit,
) {
    val userTypes = stringArrayResource(Res.array.txt_user_types)
    val selectedIndex = when (displayState.userType) {
        UserType.STUDENT -> 0
        UserType.TEACHER -> 1
        else -> 0 // Using Student Sign Up as default
    }

    val (changedEmail, onChangeEmail) = remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MaterialButtonToggleGroup(
            items = userTypes,
            selectedIndex = selectedIndex,
            cornerRadius = 32.dp,
        ) {
            when (it) {
                0 -> UserType.STUDENT
                1 -> UserType.TEACHER
                else -> null
            }?.let { userType ->
                if (displayState.userType != userType) {
                    onUiEvent(ForgotPasswordScreenUiEvent.OnClickUserTypeBtn(userType))
                }
            }
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_email),
            value = changedEmail,
            inputType = KeyboardType.Email,
            trailingIcon = {
                Icon(Icons.Default.Email, contentDescription = "")
            },
            isError = displayState.validationError.isNullOrEmpty().not(),
            supportingText = displayState.validationError,
        ) {
            onChangeEmail(it)
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            TextButton(
                onClick = {
                    onUiEvent(ForgotPasswordScreenUiEvent.OnClickSendResetLink(changedEmail))
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            ) {
                Text(
                    stringResource(Res.string.txt_send_reset_link),
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }
        }
        Text(text = stringResource(Res.string.hint_password_recovery))
    }
}

@Composable
private fun ForgotPasswordFooterContent(
    modifier: Modifier = Modifier,
    onUiEvent: (ForgotPasswordScreenUiEvent) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.padding(top = 16.dp))
        HorizontalDividerWithLabel(
            modifier = Modifier.padding(vertical = 16.dp),
            label = stringResource(Res.string.txt_or)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                stringResource(Res.string.hint_remembered_password),
                modifier = Modifier.padding(end = 8.dp),
            )
            TextButton(
                onClick = { onUiEvent(ForgotPasswordScreenUiEvent.OnClickSignIn) }
            ) {
                Text(text = stringResource(Res.string.txt_sign_in))
            }
        }
    }
}
