package com.workfort.pstuian.view.ui.blooddonationrequestcreate

import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.model.BloodDonationRequestInput
import com.workfort.pstuian.model.BloodDonationRequestInputError
import com.workfort.pstuian.reducer.ui.blooddonationrequestcreate.BloodDonationRequestCreateScreenState
import com.workfort.pstuian.util.DateUtil
import com.workfort.pstuian.view.ui.common.component.AppBar
import com.workfort.pstuian.view.ui.common.component.DropDownMenuBox
import com.workfort.pstuian.view.ui.common.component.OutlinedTextInput
import com.workfort.pstuian.view.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.view.ui.common.component.ShowLoaderDialog
import com.workfort.pstuian.view.ui.common.component.ShowSuccessDialog
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import pstuian.shared.generated.resources.Res
import pstuian.shared.generated.resources.blood_group
import pstuian.shared.generated.resources.helper_text_help_message_max_length
import pstuian.shared.generated.resources.hint_blood_group
import pstuian.shared.generated.resources.hint_contact
import pstuian.shared.generated.resources.hint_message
import pstuian.shared.generated.resources.hint_need_before
import pstuian.shared.generated.resources.info_blood_donation
import pstuian.shared.generated.resources.label_create_blood_donation_request_screen
import pstuian.shared.generated.resources.txt_home
import pstuian.shared.generated.resources.txt_send


@Composable
fun BloodDonationRequestCreateScreen(
    modifier: Modifier = Modifier,
    screenState: BloodDonationRequestCreateScreenState,
    onUiEvent: (BloodDonationRequestCreateScreenUiEvent) -> Unit,
) {
    screenState.displayState.Handle(modifier = modifier, onUiEvent = onUiEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    displayState: BloodDonationRequestCreateScreenState.DisplayState,
    onUiEvent: (BloodDonationRequestCreateScreenUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = stringResource(Res.string.label_create_blood_donation_request_screen),
                onClickBack = {
                    onUiEvent(BloodDonationRequestCreateScreenUiEvent.OnClickBack)
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
            ) {
                FormContent(
                    modifier,
                    displayState.input,
                    displayState.validationError,
                    onUiEvent,
                )
            }
            if (displayState.isLoading) {
                ShowLoaderDialog()
            }
        }
    }
}

@Composable
private fun FormContent(
    modifier: Modifier = Modifier,
    input: BloodDonationRequestInput,
    validationError: BloodDonationRequestInputError,
    onUiEvent: (BloodDonationRequestCreateScreenUiEvent) -> Unit,
) {
    val (changedInput, onChangeInput) = remember { mutableStateOf(input) }

    // to update the date
    LaunchedEffect(key1 = input) {
        if (input.date != changedInput.date) {
            onChangeInput(input)
        }
    }

    LaunchedEffect(key1 = changedInput) {
        onUiEvent(BloodDonationRequestCreateScreenUiEvent.OnChangeInput(changedInput))
    }

    Column(modifier = modifier.padding(vertical = 16.dp)) {
        DropDownMenuBox (
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
                    onUiEvent(BloodDonationRequestCreateScreenUiEvent.OnClickSelectDate)
                }
            },
            label = stringResource(Res.string.hint_need_before),
            value = changedInput.date,
            trailingIcon = {
                IconButton(
                    onClick = {
                        onUiEvent(BloodDonationRequestCreateScreenUiEvent.OnClickSelectDate)
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
            onClick = { onUiEvent(BloodDonationRequestCreateScreenUiEvent.OnClickSend) },
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

@Composable
private fun BloodDonationRequestCreateScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (BloodDonationRequestCreateScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    val state = messageState
    if (state != null) {
        state.Handle(onUiEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BloodDonationRequestCreateScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (BloodDonationRequestCreateScreenUiEvent) -> Unit,
) {
    when (this) {
        is BloodDonationRequestCreateScreenState.DisplayState.MessageState.SelectDate -> {
            val state = rememberDatePickerState(
                initialDisplayMode = DisplayMode.Picker,
                selectableDates = object : SelectableDates { // only allow dates from today
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        return utcTimeMillis >= DateUtil.getTimeInMillsAtMidnight()
                    }
                },
            )
            DatePickerDialog(
                onDismissRequest = {
                    onUiEvent(BloodDonationRequestCreateScreenUiEvent.MessageConsumed)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onUiEvent(
                                BloodDonationRequestCreateScreenUiEvent.OnSelectDate(
                                    state.selectedDateMillis,
                                ),
                            )
                        }
                    ) {
                        Text("Select")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onUiEvent(BloodDonationRequestCreateScreenUiEvent.MessageConsumed)
                        }
                    ) {
                        Text("Dismiss")
                    }
                },
            ) {
                DatePicker(state = state)
            }
        }
        is BloodDonationRequestCreateScreenState.DisplayState.MessageState.SendRequestSuccess -> {
            ShowSuccessDialog(
                message = message,
                confirmButtonText = stringResource(Res.string.txt_home),
                onConfirm = {
                    onUiEvent(BloodDonationRequestCreateScreenUiEvent.MessageConsumed)
                    onUiEvent(BloodDonationRequestCreateScreenUiEvent.OnClickBack)
                },
                onDismiss = {
                    onUiEvent(BloodDonationRequestCreateScreenUiEvent.MessageConsumed)
                },
            )
        }
        is BloodDonationRequestCreateScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                message = message,
                onConfirm = {
                    onUiEvent(BloodDonationRequestCreateScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(BloodDonationRequestCreateScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}
