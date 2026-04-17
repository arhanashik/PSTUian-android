package com.workfort.pstuian.ui.forgotpassword.composable

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.composable.HorizontalDividerWithLabel
import com.workfort.pstuian.ui.common.composable.MaterialButtonToggleGroup
import com.workfort.pstuian.ui.common.composable.OutlinedTextInput
import com.workfort.pstuian.ui.forgotpassword.state.ForgotPasswordUiEvent
import com.workfort.pstuian.ui.forgotpassword.state.ForgotPasswordUiState
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.hint_email
import pstuian.feature_presentation.generated.resources.hint_password_recovery
import pstuian.feature_presentation.generated.resources.hint_remembered_password
import pstuian.feature_presentation.generated.resources.txt_or
import pstuian.feature_presentation.generated.resources.txt_send_reset_link
import pstuian.feature_presentation.generated.resources.txt_sign_in
import pstuian.feature_presentation.generated.resources.txt_user_types

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordContentPanel(
    uiState: ForgotPasswordUiState,
    onUiEvent: (ForgotPasswordUiEvent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        ForgotPasswordFormContent(uiState, onUiEvent)
        ForgotPasswordFooterContent(onUiEvent)
    }
}

@Composable
private fun ForgotPasswordFormContent(
    state: ForgotPasswordUiState,
    onUiEvent: (ForgotPasswordUiEvent) -> Unit,
) {
    val userTypes = stringArrayResource(Res.array.txt_user_types)
    val selectedIndex = when (state.userType) {
        UserType.STUDENT -> 0
        UserType.TEACHER -> 1
        else -> 0
    }

    val (changedEmail, onChangeEmail) = remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                if (state.userType != userType) {
                    onUiEvent(ForgotPasswordUiEvent.UserTypeBtnClicked(userType))
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
            isError = state.validationError.isNullOrEmpty().not(),
            supportingText = state.validationError,
        ) {
            onChangeEmail(it)
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            TextButton(
                onClick = {
                    onUiEvent(ForgotPasswordUiEvent.SendResetLinkClicked(changedEmail))
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
    onUiEvent: (ForgotPasswordUiEvent) -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                onClick = { onUiEvent(ForgotPasswordUiEvent.SignInClicked) }
            ) {
                Text(text = stringResource(Res.string.txt_sign_in))
            }
        }
    }
}
