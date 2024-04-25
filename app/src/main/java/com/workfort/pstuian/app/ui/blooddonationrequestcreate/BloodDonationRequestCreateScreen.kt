package com.workfort.pstuian.app.ui.blooddonationrequestcreate

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.common.component.AppBar
import com.workfort.pstuian.app.ui.common.component.DropDownMenuBox
import com.workfort.pstuian.app.ui.common.component.OutlinedTextInput
import com.workfort.pstuian.app.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.app.ui.common.component.ShowLoaderDialog
import com.workfort.pstuian.app.ui.common.component.ShowSuccessDialog
import com.workfort.pstuian.model.BloodDonationRequestInput
import com.workfort.pstuian.model.BloodDonationRequestInputError
import com.workfort.pstuian.util.DateUtil
import java.util.Calendar


@Composable
fun BloodDonationRequestCreateScreen(
    modifier: Modifier = Modifier,
    viewModel: BloodDonationRequestCreateViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<BloodDonationRequestCreateScreenUiEvent>(
            BloodDonationRequestCreateScreenUiEvent.None,
        )
    }

    with(screenState) {
        displayState.Handle(modifier = modifier) {
            uiEvent = it
        }
        navigationState?.Handle(navController) {
            uiEvent = it
        }
    }

    with(uiEvent) {
        when (this) {
            is BloodDonationRequestCreateScreenUiEvent.None -> Unit
            is BloodDonationRequestCreateScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is BloodDonationRequestCreateScreenUiEvent.OnClickSelectDate ->
                viewModel.onClickSelectDate()
            is BloodDonationRequestCreateScreenUiEvent.OnClickSend -> viewModel.sendRequest()
            is BloodDonationRequestCreateScreenUiEvent.OnChangeInput ->
                viewModel.onChangeInput(input)
            is BloodDonationRequestCreateScreenUiEvent.OnSelectDate ->
                viewModel.onSelectDate(dateMills)
            is BloodDonationRequestCreateScreenUiEvent.MessageConsumed ->
                viewModel.messageConsumed()
            is BloodDonationRequestCreateScreenUiEvent.NavigationConsumed ->
                viewModel.navigationConsumed()
        }
        uiEvent = BloodDonationRequestCreateScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    displayState: BloodDonationRequestCreateScreenState.DisplayState,
    onUiEvent: (BloodDonationRequestCreateScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = context.getString(R.string.label_create_blood_donation_request_screen),
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
    val context = LocalContext.current
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
        DropDownMenuBox(
            anchorView = { modifier, expanded ->
                OutlinedTextInput(
                    modifier = modifier,
                    label = context.getString(R.string.hint_blood_group),
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
            items = context.resources.getStringArray(R.array.blood_group),
        ) {
            onChangeInput(changedInput.copy(bloodGroup = it))
        }
        OutlinedTextInput(
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) {
                    onUiEvent(BloodDonationRequestCreateScreenUiEvent.OnClickSelectDate)
                }
            },
            label = context.getString(R.string.hint_need_before),
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
            label = context.getString(R.string.hint_contact),
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
            label = context.getString(R.string.hint_message),
            value = changedInput.message,
            singleLine = false,
            minLines = 8,
            maxLines = 10,
            isError = validationError.message.isNotEmpty(),
            supportingText = validationError.message.ifEmpty {
                context.getString(R.string.helper_text_help_message_max_length)
            },
        ) {
            onChangeInput(changedInput.copy(message = it))
        }
        TextButton(
            onClick = { onUiEvent(BloodDonationRequestCreateScreenUiEvent.OnClickSend) },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        ) {
            Text(
                context.getString(R.string.txt_send),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
        Text(text = context.getString(R.string.info_blood_donation))
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
    messageState?.Handle(onUiEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BloodDonationRequestCreateScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (BloodDonationRequestCreateScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    when (this) {
        is BloodDonationRequestCreateScreenState.DisplayState.MessageState.SelectDate -> {
            val state = rememberDatePickerState(
                initialDisplayMode = DisplayMode.Picker,
                selectableDates = object : SelectableDates { // only allow dates from today
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        return utcTimeMillis >= DateUtil.getTimeInMillsAtMidnight()
                    }

                    override fun isSelectableYear(year: Int): Boolean {
                        return year >= Calendar.getInstance().get(Calendar.YEAR)
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
                confirmButtonText = context.getString(R.string.txt_home),
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

@Composable
private fun BloodDonationRequestCreateScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (BloodDonationRequestCreateScreenUiEvent) -> Unit,
) {
    when (this) {
        is BloodDonationRequestCreateScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
    }
    onUiEvent(BloodDonationRequestCreateScreenUiEvent.NavigationConsumed)
}
