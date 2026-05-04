package com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.BloodDonationRequestInput
import com.workfort.pstuian.featuredomain.model.BloodDonationRequestInputError
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate.state.BloodDonationRequestCreateUiEvent
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate.state.BloodDonationRequestCreateUiState
import com.workfort.pstuian.ui.common.composable.ActionButton
import com.workfort.pstuian.ui.common.theme.TextStyle
import com.workfort.pstuian.ui.signin.composable.AuthUnderlinedExposedDropdown
import com.workfort.pstuian.ui.signin.composable.AuthUnderlinedField
import com.workfort.pstuian.ui.signin.screendata.AuthFormFieldSpacing
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.blood_group
import pstuian.feature_presentation.generated.resources.helper_text_help_message_max_length
import pstuian.feature_presentation.generated.resources.hint_blood_group
import pstuian.feature_presentation.generated.resources.hint_contact
import pstuian.feature_presentation.generated.resources.hint_message
import pstuian.feature_presentation.generated.resources.hint_need_before
import pstuian.feature_presentation.generated.resources.info_blood_donation
import pstuian.feature_presentation.generated.resources.txt_send

@Composable
internal fun BloodDonationRequestCreateContentPanel(
    uiState: BloodDonationRequestCreateUiState.Content,
    onUiEvent: (BloodDonationRequestCreateUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        FormContent(
            input = uiState.input,
            validationError = uiState.validationError,
            onUiEvent = onUiEvent,
        )
    }
}

@Composable
private fun FormContent(
    input: BloodDonationRequestInput,
    validationError: BloodDonationRequestInputError,
    onUiEvent: (BloodDonationRequestCreateUiEvent) -> Unit,
) {
    val (changedInput, onChangeInput) = remember { mutableStateOf(input) }

    LaunchedEffect(key1 = input) {
        if (input.date != changedInput.date) {
            onChangeInput(input)
        }
    }

    LaunchedEffect(key1 = changedInput) {
        onUiEvent(BloodDonationRequestCreateUiEvent.InputChanged(changedInput))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
    ) {
        AuthUnderlinedExposedDropdown(
            label = stringResource(Res.string.hint_blood_group),
            value = changedInput.bloodGroup,
            items = stringArrayResource(Res.array.blood_group).toTypedArray(),
            onItemSelected = { onChangeInput(changedInput.copy(bloodGroup = it)) },
            isError = validationError.bloodGroup.isNotEmpty(),
            errorText = validationError.bloodGroup.takeIf { it.isNotEmpty() },
        )
        Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
        AuthUnderlinedField(
            label = stringResource(Res.string.hint_need_before),
            value = changedInput.date,
            onValueChange = { },
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) { onUiEvent(BloodDonationRequestCreateUiEvent.SelectDateClicked) }
            },
            readOnly = true,
            isError = validationError.date.isNotEmpty(),
            supportingText = validationError.date.takeIf { it.isNotEmpty() },
            trailingContent = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                    modifier = Modifier
                        .size(22.dp)
                        .clickable {
                            onUiEvent(BloodDonationRequestCreateUiEvent.SelectDateClicked)
                        },
                )
            },
        )
        Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
        AuthUnderlinedField(
            label = stringResource(Res.string.hint_contact),
            value = changedInput.contact,
            onValueChange = { onChangeInput(changedInput.copy(contact = it)) },
            trailingContent = { Icon(imageVector = Icons.Default.Phone, contentDescription = "") },
            isError = validationError.contact.isNotEmpty(),
            supportingText = validationError.contact.takeIf { it.isNotEmpty() }
                ?: "e.g 01xxxxxxxxx, 02xxxxxxxxx",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions.Default,
        )
        Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
        AuthUnderlinedField(
            label = stringResource(Res.string.hint_message),
            value = changedInput.message,
            onValueChange = { onChangeInput(changedInput.copy(message = it)) },
            isError = validationError.message.isNotEmpty(),
            supportingText = validationError.message.takeIf { it.isNotEmpty() }
                ?: stringResource(Res.string.helper_text_help_message_max_length),
            singleLine = false,
            minLines = 5,
            maxLines = 8,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Default,
            ),
            keyboardActions = KeyboardActions.Default,
        )
        Spacer(modifier = Modifier.height(28.dp))
        ActionButton(
            label = stringResource(Res.string.txt_send).uppercase(),
            icon = Icons.AutoMirrored.Filled.ArrowForward,
            onClick = { onUiEvent(BloodDonationRequestCreateUiEvent.SendClicked(input)) },
        )
        Spacer(modifier = Modifier.height(20.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        ) {
            Text(
                text = stringResource(Res.string.info_blood_donation),
                modifier = Modifier.padding(16.dp),
                style = TextStyle.body2.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            )
        }
    }
}
