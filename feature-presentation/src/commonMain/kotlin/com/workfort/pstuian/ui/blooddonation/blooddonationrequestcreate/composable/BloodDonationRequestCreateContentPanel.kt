package com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.BloodDonationRequestInput
import com.workfort.pstuian.featuredomain.model.BloodDonationRequestInputError
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate.state.BloodDonationRequestCreateUiEvent
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate.state.BloodDonationRequestCreateUiState
import com.workfort.pstuian.ui.common.composable.DropDownMenuBox
import com.workfort.pstuian.ui.common.composable.OutlinedTextInput
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
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
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
    modifier: Modifier = Modifier,
    input: BloodDonationRequestInput,
    validationError: BloodDonationRequestInputError,
    onUiEvent: (BloodDonationRequestCreateUiEvent) -> Unit,
) {
    val (changedInput, onChangeInput) = remember { mutableStateOf(input) }

    // to update the date
    LaunchedEffect(key1 = input) {
        if (input.date != changedInput.date) {
            onChangeInput(input)
        }
    }

    LaunchedEffect(key1 = changedInput) {
        onUiEvent(BloodDonationRequestCreateUiEvent.InputChanged(changedInput))
    }

    Column(modifier = modifier.padding(vertical = 16.dp)) {
        DropDownMenuBox(
            anchorView = { modifier, expanded ->
                OutlinedTextInput(
                    modifier = modifier,
                    label = stringResource(Res.string.hint_blood_group),
                    value = changedInput.bloodGroup,
                    readOnly = true,
                    isError = validationError.bloodGroup.isNotEmpty(),
                    supportingText = validationError.bloodGroup,
                    trailingIcon = {
                        Icon(
                            if (expanded) {
                                Icons.Default.KeyboardArrowUp
                            } else {
                                Icons.Default.KeyboardArrowDown
                            },
                            contentDescription = "",
                        )
                    },
                    onValueChange = { }
                )
            },
            items = stringArrayResource(Res.array.blood_group).toTypedArray(),
        ) {
            onChangeInput(changedInput.copy(bloodGroup = it))
        }
        OutlinedTextInput(
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) {
                    onUiEvent(BloodDonationRequestCreateUiEvent.SelectDateClicked)
                }
            },
            label = stringResource(Res.string.hint_need_before),
            value = changedInput.date,
            trailingIcon = {
                IconButton(
                    onClick = {
                        onUiEvent(BloodDonationRequestCreateUiEvent.SelectDateClicked)
                    },
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = "")
                }
            },
            readOnly = true,
            isError = validationError.date.isNotEmpty(),
            supportingText = validationError.date,
        ) {
            onChangeInput(changedInput.copy(date = it))
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_contact),
            value = changedInput.contact,
            trailingIcon = {
                Icon(Icons.Default.Call, contentDescription = "")
            },
            isError = validationError.contact.isNotEmpty(),
            supportingText = validationError.contact.ifEmpty {
                "e.g 01xxxxxxxxx, 02xxxxxxxxx"
            },
        ) {
            onChangeInput(changedInput.copy(contact = it))
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_message),
            value = changedInput.message,
            singleLine = false,
            minLines = 8,
            maxLines = 10,
            isError = validationError.message.isNotEmpty(),
            supportingText = validationError.message.ifEmpty {
                stringResource(Res.string.helper_text_help_message_max_length)
            },
        ) {
            onChangeInput(changedInput.copy(message = it))
        }
        TextButton(
            onClick = { onUiEvent(BloodDonationRequestCreateUiEvent.SendClicked) },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        ) {
            Text(
                stringResource(Res.string.txt_send),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
        Text(text = stringResource(Res.string.info_blood_donation))
    }
}
