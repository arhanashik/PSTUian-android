package com.workfort.pstuian.view.ui.emailverification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.reducer.ui.emailverification.EmailVerificationScreenState
import com.workfort.pstuian.view.ui.common.component.AppBar
import com.workfort.pstuian.view.ui.common.component.HorizontalDividerWithLabel
import com.workfort.pstuian.view.ui.common.component.MaterialButtonToggleGroup
import com.workfort.pstuian.view.ui.common.component.OutlinedTextInput
import com.workfort.pstuian.view.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.view.ui.common.component.ShowLoaderDialog
import com.workfort.pstuian.view.ui.common.component.ShowSuccessDialog
import com.workfort.pstuian.view.ui.common.theme.btnBgDefault
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import pstuian.shared.generated.resources.Res
import pstuian.shared.generated.resources.hint_already_verified
import pstuian.shared.generated.resources.hint_email
import pstuian.shared.generated.resources.hint_email_verification_link
import pstuian.shared.generated.resources.label_email_verification_screen
import pstuian.shared.generated.resources.txt_or
import pstuian.shared.generated.resources.txt_send_verification_email
import pstuian.shared.generated.resources.txt_sign_in
import pstuian.shared.generated.resources.txt_user_types


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailVerificationScreen(
    modifier: Modifier = Modifier,
    screenState: EmailVerificationScreenState,
    onUiEvent: (EmailVerificationScreenUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = stringResource(Res.string.label_email_verification_screen),
                onClickBack = {
                    onUiEvent(EmailVerificationScreenUiEvent.OnClickBack)
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
                EmailVerificationFormContent(modifier, screenState.displayState, onUiEvent)
                EmailVerificationFooterContent(modifier, onUiEvent)
            }
            if (screenState.displayState.isLoading) {
                ShowLoaderDialog()
            }
        }
    }

    screenState.displayState.messageState?.let { messageState ->
        when (messageState) {
            is EmailVerificationScreenState.DisplayState.MessageState.EmailSentSuccess -> {
                ShowSuccessDialog(
                    message = messageState.message,
                    confirmButtonText = stringResource(Res.string.txt_sign_in),
                    cancelable = false,
                    onConfirm = {
                        onUiEvent(EmailVerificationScreenUiEvent.MessageConsumed)
                        onUiEvent(EmailVerificationScreenUiEvent.OnClickBack)
                    }
                )
            }
            is EmailVerificationScreenState.DisplayState.MessageState.Error -> {
                ShowErrorDialog(
                    message = messageState.message,
                    dismissButtonText = null,
                    onConfirm = {
                        onUiEvent(EmailVerificationScreenUiEvent.MessageConsumed)
                    },
                    onDismiss = { /* NO OP */ }
                )
            }
        }
    }
}

@Composable
private fun EmailVerificationFormContent(
    modifier: Modifier = Modifier,
    displayState: EmailVerificationScreenState.DisplayState,
    onUiEvent: (EmailVerificationScreenUiEvent) -> Unit,
) {
    val userTypes = stringArrayResource(Res.array.txt_user_types)
    val selectedIndex = when (displayState.userType) {
        UserType.STUDENT -> 0
        UserType.TEACHER -> 1
        else -> 0 // Using Student as default
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
                    onUiEvent(EmailVerificationScreenUiEvent.OnClickUserTypeBtn(userType))
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .btnBgDefault()
                .clickable {
                    onUiEvent(EmailVerificationScreenUiEvent.OnClickSendEmail(changedEmail))
                }
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                stringResource(Res.string.txt_send_verification_email),
                color = Color.White,
            )
        }
        Text(text = stringResource(Res.string.hint_email_verification_link))
    }
}

@Composable
private fun EmailVerificationFooterContent(
    modifier: Modifier = Modifier,
    onUiEvent: (EmailVerificationScreenUiEvent) -> Unit,
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
                stringResource(Res.string.hint_already_verified),
                modifier = Modifier.padding(end = 8.dp),
            )
            TextButton(
                onClick = { onUiEvent(EmailVerificationScreenUiEvent.OnClickSignIn) }
            ) {
                Text(text = stringResource(Res.string.txt_sign_in))
            }
        }
    }
}
