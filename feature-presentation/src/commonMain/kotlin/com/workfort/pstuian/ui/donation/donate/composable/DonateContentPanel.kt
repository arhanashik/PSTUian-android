package com.workfort.pstuian.ui.donation.donate.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.composable.ActionButton
import com.workfort.pstuian.ui.common.composable.InfoBox
import com.workfort.pstuian.ui.donation.donate.state.DonateUiEvent
import com.workfort.pstuian.ui.donation.donate.state.DonateUiState
import com.workfort.pstuian.ui.signin.composable.AuthUnderlinedField
import com.workfort.pstuian.ui.signin.screendata.AuthFormFieldSpacing
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
    val donationOptionInfo = "You can send a donation to:\n${uiState.donationOption}\nAfter that, please save the information."

    LaunchedEffect(changedInput) {
        onUiEvent(DonateUiEvent.ChangeInput(changedInput))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
    ) {
        InfoBox(text = donationOptionInfo)
        Spacer(modifier = Modifier.height(20.dp))
        AuthUnderlinedField(
            label = stringResource(Res.string.hint_name),
            value = changedInput.name,
            onValueChange = { onChangeInput(changedInput.copy(name = it)) },
            isError = uiState.validationError.name.isNotEmpty(),
            supportingText = uiState.validationError.name,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
        )
        Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
        AuthUnderlinedField(
            label = stringResource(Res.string.hint_email),
            value = changedInput.email,
            onValueChange = { onChangeInput(changedInput.copy(email = it)) },
            leadingIcon = Icons.Default.Email,
            isError = uiState.validationError.email.isNotEmpty(),
            supportingText = uiState.validationError.email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
        )
        Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
        AuthUnderlinedField(
            label = stringResource(Res.string.hint_reference),
            value = changedInput.reference,
            onValueChange = { onChangeInput(changedInput.copy(reference = it)) },
            isError = uiState.validationError.reference.isNotEmpty(),
            supportingText = uiState.validationError.reference,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
        )
        Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
        AuthUnderlinedField(
            label = stringResource(Res.string.hint_message),
            value = changedInput.message,
            onValueChange = { onChangeInput(changedInput.copy(message = it)) },
            isError = uiState.validationError.message.isNotEmpty(),
            supportingText = uiState.validationError.message.ifEmpty {
                stringResource(Res.string.helper_txt_max_length_donation_message)
            },
            singleLine = false,
            minLines = 5,
            maxLines = 8,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Default,
            ),
        )
        Spacer(modifier = Modifier.height(28.dp))
        ActionButton(
            label = stringResource(Res.string.txt_send).uppercase(),
            icon = Icons.AutoMirrored.Filled.ArrowForward,
            onClick = { onUiEvent(DonateUiEvent.SendDonationInfo) },
        )
        Spacer(modifier = Modifier.height(20.dp))
        InfoBox(text = stringResource(Res.string.message_donation_thanks))
    }
}
