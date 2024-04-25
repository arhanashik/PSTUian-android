package com.workfort.pstuian.app.ui.blooddonationcreate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.common.component.AppBar
import com.workfort.pstuian.app.ui.common.component.OutlinedTextInput
import com.workfort.pstuian.app.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.app.ui.common.component.ShowLoaderDialog
import com.workfort.pstuian.app.ui.common.component.ShowSuccessDialog
import com.workfort.pstuian.model.BloodDonationInput
import com.workfort.pstuian.model.BloodDonationInputError
import com.workfort.pstuian.util.DateUtil
import java.util.Calendar


@Composable
fun BloodDonationCreateScreen(
    modifier: Modifier = Modifier,
    viewModel: BloodDonationCreateViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<BloodDonationCreateScreenUiEvent>(
            BloodDonationCreateScreenUiEvent.None,
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
            is BloodDonationCreateScreenUiEvent.None -> Unit
            is BloodDonationCreateScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is BloodDonationCreateScreenUiEvent.OnClickSelectDate ->
                viewModel.onClickSelectDate()
            is BloodDonationCreateScreenUiEvent.OnClickSend -> viewModel.sendRequest()
            is BloodDonationCreateScreenUiEvent.OnChangeInput ->
                viewModel.onChangeInput(input)
            is BloodDonationCreateScreenUiEvent.OnSelectDate ->
                viewModel.onSelectDate(dateMills)
            is BloodDonationCreateScreenUiEvent.MessageConsumed ->
                viewModel.messageConsumed()
            is BloodDonationCreateScreenUiEvent.NavigationConsumed ->
                viewModel.navigationConsumed()
        }
        uiEvent = BloodDonationCreateScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    displayState: BloodDonationCreateScreenState.DisplayState,
    onUiEvent: (BloodDonationCreateScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = context.getString(R.string.label_blood_donation_create_screen),
                onClickBack = {
                    onUiEvent(BloodDonationCreateScreenUiEvent.OnClickBack)
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
    input: BloodDonationInput,
    validationError: BloodDonationInputError,
    onUiEvent: (BloodDonationCreateScreenUiEvent) -> Unit,
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
        onUiEvent(BloodDonationCreateScreenUiEvent.OnChangeInput(changedInput))
    }

    Column(modifier = modifier.padding(vertical = 16.dp)) {
        OutlinedTextInput(
            modifier = modifier,
            label = context.getString(R.string.hint_request_id),
            value = changedInput.requestId.toString(),
            inputType = KeyboardType.Number,
            isError = validationError.requestId.isNotEmpty(),
            supportingText = validationError.requestId.ifEmpty {
                context.getString(R.string.helper_text_blood_donation_request_id)
            },
        ) {
            val requestId = it.toIntOrNull() ?: 0
            onChangeInput(changedInput.copy(requestId = requestId))
        }
        OutlinedTextInput(
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) {
                    onUiEvent(BloodDonationCreateScreenUiEvent.OnClickSelectDate)
                }
            },
            label = context.getString(R.string.hint_donation_date),
            value = changedInput.date,
            trailingIcon = {
                IconButton(
                    onClick = {
                        onUiEvent(BloodDonationCreateScreenUiEvent.OnClickSelectDate)
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
            onClick = { onUiEvent(BloodDonationCreateScreenUiEvent.OnClickSend) },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        ) {
            Text(
                context.getString(R.string.txt_send),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}

@Composable
private fun BloodDonationCreateScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (BloodDonationCreateScreenUiEvent) -> Unit,
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
private fun BloodDonationCreateScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (BloodDonationCreateScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    when (this) {
        is BloodDonationCreateScreenState.DisplayState.MessageState.SelectDate -> {
            val state = rememberDatePickerState(
                initialDisplayMode = DisplayMode.Picker,
                selectableDates = object : SelectableDates { // only allow dates until today
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        return utcTimeMillis <= DateUtil.getTimeInMillsUntilMidnight()
                    }

                    override fun isSelectableYear(year: Int): Boolean {
                        return year <= Calendar.getInstance().get(Calendar.YEAR)
                    }
                },
            )
            DatePickerDialog(
                onDismissRequest = {
                    onUiEvent(BloodDonationCreateScreenUiEvent.MessageConsumed)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onUiEvent(
                                BloodDonationCreateScreenUiEvent.OnSelectDate(
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
                            onUiEvent(BloodDonationCreateScreenUiEvent.MessageConsumed)
                        }
                    ) {
                        Text("Dismiss")
                    }
                },
            ) {
                DatePicker(state = state)
            }
        }
        is BloodDonationCreateScreenState.DisplayState.MessageState.SendRequestSuccess -> {
            ShowSuccessDialog(
                message = message,
                confirmButtonText = context.getString(R.string.txt_go_back),
                cancelable = false,
                onConfirm = {
                    onUiEvent(BloodDonationCreateScreenUiEvent.MessageConsumed)
                    onUiEvent(BloodDonationCreateScreenUiEvent.OnClickBack)
                },
                onDismiss = {
                    onUiEvent(BloodDonationCreateScreenUiEvent.MessageConsumed)
                },
            )
        }
        is BloodDonationCreateScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                message = message,
                onConfirm = {
                    onUiEvent(BloodDonationCreateScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(BloodDonationCreateScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}

@Composable
private fun BloodDonationCreateScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (BloodDonationCreateScreenUiEvent) -> Unit,
) {
    when (this) {
        is BloodDonationCreateScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
    }
    onUiEvent(BloodDonationCreateScreenUiEvent.NavigationConsumed)
}
