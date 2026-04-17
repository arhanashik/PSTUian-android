package com.workfort.pstuian.ui.donate.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.composable.OutlinedTextInput
import com.workfort.pstuian.ui.common.theme.TextStyle
import com.workfort.pstuian.ui.common.theme.btnBgDefault
import com.workfort.pstuian.ui.donate.state.DonateUiEvent
import com.workfort.pstuian.ui.donate.state.DonateUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.helper_txt_max_length_donation_message
import pstuian.feature_presentation.generated.resources.hint_email
import pstuian.feature_presentation.generated.resources.hint_message
import pstuian.feature_presentation.generated.resources.hint_name
import pstuian.feature_presentation.generated.resources.hint_reference
import pstuian.feature_presentation.generated.resources.message_donation_thanks
import pstuian.feature_presentation.generated.resources.txt_send

@Composable
internal fun DonateContentPanel(
    uiState: DonateUiState,
    onUiEvent: (DonateUiEvent) -> Unit,
) {
    DonateFormContent(
        uiState = uiState,
        onUiEvent = onUiEvent,
    )
}

@Composable
private fun DonateFormContent(
    uiState: DonateUiState,
    onUiEvent: (DonateUiEvent) -> Unit,
) {
    val (changedInput, onChangeInput) = remember(uiState.donationInput) {
        mutableStateOf(uiState.donationInput)
    }
    val donationOptionInfo = "You can send a donation to:<br>${uiState.donationOption}<br>After that, " +
            "please save the information."

    LaunchedEffect(changedInput) {
        onUiEvent(DonateUiEvent.ChangeInput(changedInput))
    }

    Column(modifier = Modifier.padding(vertical = 16.dp)) {
//        HtmlText(html = donationOptionInfo)
        Text(donationOptionInfo, style = TextStyle.body2)
        OutlinedTextInput(
            label = stringResource(Res.string.hint_name),
            value = changedInput.name,
            isError = uiState.validationError.name.isNotEmpty(),
            supportingText = uiState.validationError.name,
        ) {
            onChangeInput(changedInput.copy(name = it))
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_email),
            value = changedInput.email,
            inputType = KeyboardType.Email,
            trailingIcon = {
                Icon(Icons.Default.Email, contentDescription = "")
            },
            isError = uiState.validationError.email.isNotEmpty(),
            supportingText = uiState.validationError.email,
        ) {
            onChangeInput(changedInput.copy(email = it))
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_reference),
            value = changedInput.reference,
            isError = uiState.validationError.reference.isNotEmpty(),
            supportingText = uiState.validationError.reference,
        ) {
            onChangeInput(changedInput.copy(reference = it))
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_message),
            value = changedInput.message,
            singleLine = false,
            minLines = 8,
            maxLines = 10,
            isError = uiState.validationError.message.isNotEmpty(),
            supportingText = uiState.validationError.message.ifEmpty {
                stringResource(Res.string.helper_txt_max_length_donation_message)
            },
        ) {
            onChangeInput(changedInput.copy(message = it))
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .btnBgDefault()
                .clickable { onUiEvent(DonateUiEvent.SendDonationInfo) }
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                stringResource(Res.string.txt_send),
                color = Color.White,
            )
        }
        Text(text = stringResource(Res.string.message_donation_thanks))
    }
}
