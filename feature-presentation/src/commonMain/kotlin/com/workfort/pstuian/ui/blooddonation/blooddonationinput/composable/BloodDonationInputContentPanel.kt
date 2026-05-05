package com.workfort.pstuian.ui.blooddonation.blooddonationinput.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.blooddonation.blooddonationinput.state.BloodDonationInputUiEvent
import com.workfort.pstuian.ui.blooddonation.blooddonationinput.state.BloodDonationInputUiState
import com.workfort.pstuian.ui.common.composable.ActionButton
import com.workfort.pstuian.ui.signin.composable.AuthUnderlinedField
import com.workfort.pstuian.ui.signin.screendata.AuthFormFieldSpacing
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.helper_text_blood_donation_request_id
import pstuian.feature_presentation.generated.resources.helper_text_help_message_max_length
import pstuian.feature_presentation.generated.resources.hint_donation_date
import pstuian.feature_presentation.generated.resources.hint_message
import pstuian.feature_presentation.generated.resources.hint_request_id
import pstuian.feature_presentation.generated.resources.txt_send

@Composable
internal fun BloodDonationInputContentPanel(
    uiState: BloodDonationInputUiState.Content,
    onUiEvent: (BloodDonationInputUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
        ) {
            AuthUnderlinedField(
                label = stringResource(Res.string.hint_request_id),
                value = uiState.inputData.requestId.toString(),
                onValueChange = {
                    val requestId = it.toIntOrNull() ?: 0
                    onUiEvent(BloodDonationInputUiEvent.InputChanged(uiState.inputData.copy(requestId = requestId)))
                },
                supportingText = stringResource(Res.string.helper_text_blood_donation_request_id),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions.Default,
            )
            Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
            AuthUnderlinedField(
                modifier = Modifier.onFocusChanged {
                    if (it.isFocused) {
                        onUiEvent(BloodDonationInputUiEvent.SelectDateClicked(uiState.inputData))
                    }
                },
                label = stringResource(Res.string.hint_donation_date),
                value = uiState.inputData.formattedDate,
                onValueChange = { },
                trailingContent = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                        modifier = Modifier
                            .size(22.dp)
                            .clickable {
                                onUiEvent(BloodDonationInputUiEvent.SelectDateClicked(uiState.inputData))
                            },
                    )
                },
                readOnly = true,
            )
            Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
            AuthUnderlinedField(
                label = stringResource(Res.string.hint_message),
                value = uiState.inputData.info,
                onValueChange = { info ->
                    onUiEvent(BloodDonationInputUiEvent.InputChanged(uiState.inputData.copy(info = info)))
                },
                singleLine = false,
                minLines = 5,
                maxLines = 10,
                supportingText = stringResource(Res.string.helper_text_help_message_max_length),
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
                enabled = uiState.enableSendButton,
                onClick = { onUiEvent(BloodDonationInputUiEvent.SendClicked(uiState.inputData)) },
            )
        }
    }
}