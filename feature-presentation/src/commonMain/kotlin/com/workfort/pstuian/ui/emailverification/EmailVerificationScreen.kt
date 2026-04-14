package com.workfort.pstuian.ui.emailverification

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
import com.workfort.pstuian.common.composable.AppBar
import com.workfort.pstuian.common.composable.HorizontalDividerWithLabel
import com.workfort.pstuian.common.composable.MaterialButtonToggleGroup
import com.workfort.pstuian.common.composable.OutlinedTextInput
import com.workfort.pstuian.common.composable.ShowErrorDialog
import com.workfort.pstuian.common.composable.ShowLoaderDialog
import com.workfort.pstuian.common.composable.ShowSuccessDialog
import com.workfort.pstuian.common.theme.btnBgDefault
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.emailverification.state.EmailVerificationUiEvent
import com.workfort.pstuian.ui.emailverification.state.EmailVerificationUiState
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.hint_already_verified
import pstuian.feature_presentation.generated.resources.hint_email
import pstuian.feature_presentation.generated.resources.hint_email_verification_link
import pstuian.feature_presentation.generated.resources.label_email_verification_screen
import pstuian.feature_presentation.generated.resources.txt_or
import pstuian.feature_presentation.generated.resources.txt_send_verification_email
import pstuian.feature_presentation.generated.resources.txt_sign_in
import pstuian.feature_presentation.generated.resources.txt_user_types

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailVerificationScreen(
    modifier: Modifier = Modifier,
    screenState: EmailVerificationUiState,
    onUiEvent: (EmailVerificationUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = stringResource(Res.string.label_email_verification_screen),
                onClickBack = {
                    onUiEvent(EmailVerificationUiEvent.OnClickBack)
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
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                EmailVerificationFormContent(
                    userType = screenState.userType,
                    validationError = screenState.validationError,
                    onUiEvent = onUiEvent
                )
                EmailVerificationFooterContent(onUiEvent = onUiEvent)
            }
            if (screenState.isLoading) {
                ShowLoaderDialog()
            }
        }
    }

    screenState.messageState?.let { messageState ->
        HandleMessageState(messageState, onUiEvent)
    }
}

@Composable
private fun EmailVerificationFormContent(
    userType: UserType,
    validationError: String?,
    onUiEvent: (EmailVerificationUiEvent) -> Unit,
) {
    val userTypes = stringArrayResource(Res.array.txt_user_types)
    val selectedIndex = when (userType) {
        UserType.STUDENT -> 0
        UserType.TEACHER -> 1
    }

    val (changedEmail, onChangeEmail) = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MaterialButtonToggleGroup(
            items = userTypes.toList(),
            selectedIndex = selectedIndex,
            cornerRadius = 32.dp,
        ) {
            val newUserType = when (it) {
                0 -> UserType.STUDENT
                1 -> UserType.TEACHER
                else -> UserType.STUDENT
            }
            if (userType != newUserType) {
                onUiEvent(EmailVerificationUiEvent.OnClickUserTypeBtn(newUserType))
            }
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_email),
            value = changedEmail,
            inputType = KeyboardType.Email,
            trailingIcon = {
                Icon(Icons.Default.Email, contentDescription = "")
            },
            isError = validationError.isNullOrEmpty().not(),
            supportingText = validationError ?: "",
        ) {
            onChangeEmail(it)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .btnBgDefault()
                .clickable {
                    onUiEvent(EmailVerificationUiEvent.OnClickSendEmail(changedEmail))
                }
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                stringResource(Res.string.txt_send_verification_email),
                color = Color.White,
            )
        }
        Text(
            text = stringResource(Res.string.hint_email_verification_link),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun EmailVerificationFooterContent(
    onUiEvent: (EmailVerificationUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
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
                onClick = { onUiEvent(EmailVerificationUiEvent.OnClickSignIn) }
            ) {
                Text(text = stringResource(Res.string.txt_sign_in))
            }
        }
    }
}

@Composable
private fun HandleMessageState(
    messageState: EmailVerificationUiState.MessageState,
    onUiEvent: (EmailVerificationUiEvent) -> Unit,
) {
    when (messageState) {
        is EmailVerificationUiState.MessageState.EmailSentSuccess -> {
            ShowSuccessDialog(
                message = messageState.message,
                confirmButtonText = stringResource(Res.string.txt_sign_in),
                cancelable = false,
                onConfirm = {
                    onUiEvent(EmailVerificationUiEvent.MessageConsumed)
                    onUiEvent(EmailVerificationUiEvent.OnClickBack)
                }
            )
        }
        is EmailVerificationUiState.MessageState.Error -> {
            ShowErrorDialog(
                message = messageState.message,
                dismissButtonText = null,
                onConfirm = {
                    onUiEvent(EmailVerificationUiEvent.MessageConsumed)
                },
                onDismiss = { /* NO OP */ }
            )
        }
    }
}
