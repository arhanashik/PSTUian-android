package com.workfort.pstuian.ui.blooddonationcreate.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.common.composable.OutlinedTextInput
import com.workfort.pstuian.ui.blooddonationcreate.state.BloodDonationCreateUiEvent
import com.workfort.pstuian.ui.blooddonationcreate.state.BloodDonationCreateUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.helper_text_blood_donation_request_id
import pstuian.feature_presentation.generated.resources.helper_text_help_message_max_length
import pstuian.feature_presentation.generated.resources.hint_donation_date
import pstuian.feature_presentation.generated.resources.hint_message
import pstuian.feature_presentation.generated.resources.hint_request_id
import pstuian.feature_presentation.generated.resources.txt_send

@Composable
internal fun BloodDonationCreateContentPanel(
    uiState: BloodDonationCreateUiState.Content,
    onUiEvent: (BloodDonationCreateUiEvent) -> Unit,
) {
    Column {
        OutlinedTextInput(
            label = stringResource(Res.string.hint_request_id),
            value = uiState.requestId.toString(),
            inputType = KeyboardType.Number,
            supportingText = stringResource(Res.string.helper_text_blood_donation_request_id),
        ) {
            val requestId = it.toIntOrNull() ?: 0
            onUiEvent(BloodDonationCreateUiEvent.RequestIdChanged(requestId))
        }
        OutlinedTextInput(
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) {
                    onUiEvent(BloodDonationCreateUiEvent.SelectDateClicked)
                }
            },
            label = stringResource(Res.string.hint_donation_date),
            value = uiState.formattedDate,
            trailingIcon = {
                IconButton(
                    onClick = {
                        onUiEvent(BloodDonationCreateUiEvent.SelectDateClicked)
                    },
                ) { Icon(Icons.Default.DateRange, contentDescription = "") }
            },
            readOnly = true,
//            isError = validationError.date.isNotEmpty(),
//            supportingText = validationError.date,
        ) {
            onUiEvent(BloodDonationCreateUiEvent.SelectDateClicked)
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_message),
            value = uiState.info,
            singleLine = false,
            minLines = 8,
            maxLines = 10,
            supportingText = stringResource(Res.string.helper_text_help_message_max_length),
        ) {
            onUiEvent(BloodDonationCreateUiEvent.InfoChanged(it))
        }
        TextButton(
            enabled = uiState.enableSendButton,
            onClick = { onUiEvent(BloodDonationCreateUiEvent.SendClicked) },
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