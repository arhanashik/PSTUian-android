package com.workfort.pstuian.ui.contactus.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.common.composable.OutlinedTextInput
import com.workfort.pstuian.featuredomain.model.ContactUsInput
import com.workfort.pstuian.featuredomain.model.ContactUsInputValidationError
import com.workfort.pstuian.ui.contactus.state.ContactUsUiEvent
import com.workfort.pstuian.ui.contactus.state.ContactUsUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.helper_text_help_message_max_length
import pstuian.feature_presentation.generated.resources.hint_email
import pstuian.feature_presentation.generated.resources.hint_message
import pstuian.feature_presentation.generated.resources.hint_name
import pstuian.feature_presentation.generated.resources.txt_send

@Composable
internal fun ContactUsContentPanel(
    modifier: Modifier = Modifier,
    uiState: ContactUsUiState.Content,
    onUiEvent: (ContactUsUiEvent) -> Unit,
) {
    ContactUsFormContent(
        modifier = modifier.padding(16.dp),
        contactUsInput = uiState.input,
        validationError = uiState.validationError,
        onUiEvent = onUiEvent,
    )
}

@Composable
private fun ContactUsFormContent(
    modifier: Modifier = Modifier,
    contactUsInput: ContactUsInput,
    validationError: ContactUsInputValidationError,
    onUiEvent: (ContactUsUiEvent) -> Unit,
) {
    val (changedContactUsInput, onChangeInput) = remember(contactUsInput) {
        mutableStateOf(contactUsInput)
    }

    LaunchedEffect(changedContactUsInput) {
        onUiEvent(ContactUsUiEvent.OnChangeInput(changedContactUsInput))
    }

    Column(modifier = modifier) {
        OutlinedTextInput(
            label = stringResource(Res.string.hint_name),
            value = changedContactUsInput.name,
            isError = validationError.name.isNotEmpty(),
            supportingText = validationError.name,
        ) {
            onChangeInput(changedContactUsInput.copy(name = it))
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_email),
            value = changedContactUsInput.email,
            inputType = KeyboardType.Email,
            trailingIcon = {
                Icon(Icons.Default.Email, contentDescription = "")
            },
            isError = validationError.email.isNotEmpty(),
            supportingText = validationError.email,
        ) {
            onChangeInput(changedContactUsInput.copy(email = it))
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_message),
            value = changedContactUsInput.message,
            singleLine = false,
            minLines = 8,
            maxLines = 10,
            isError = validationError.message.isNotEmpty(),
            supportingText = validationError.message.ifEmpty {
                stringResource(Res.string.helper_text_help_message_max_length)
            },
        ) {
            onChangeInput(changedContactUsInput.copy(message = it))
        }
        TextButton(
            onClick = { onUiEvent(ContactUsUiEvent.OnClickSend) },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        ) {
            Text(
                stringResource(Res.string.txt_send),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}
