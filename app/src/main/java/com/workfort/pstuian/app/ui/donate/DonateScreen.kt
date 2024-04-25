package com.workfort.pstuian.app.ui.donate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.common.component.AppBar
import com.workfort.pstuian.app.ui.common.component.HtmlText
import com.workfort.pstuian.app.ui.common.component.OutlinedTextInput
import com.workfort.pstuian.app.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.app.ui.common.component.ShowLoaderDialog
import com.workfort.pstuian.app.ui.common.component.ShowSuccessDialog
import com.workfort.pstuian.model.DonationInput
import com.workfort.pstuian.model.DonationInputValidationError


@Composable
fun DonateScreen(
    modifier: Modifier = Modifier,
    viewModel: DonateViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<DonateScreenUiEvent>(DonateScreenUiEvent.None)
    }

    LaunchedEffect(key1 = null) {
        viewModel.loadDonationOptions()
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
            is DonateScreenUiEvent.None -> Unit
            is DonateScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is DonateScreenUiEvent.OnClickSend -> viewModel.sendDonationInfo()
            is DonateScreenUiEvent.OnChangeInput ->
                viewModel.onChangeInput(input)
            is DonateScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is DonateScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = DonateScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    displayState: DonateScreenState.DisplayState,
    onUiEvent: (DonateScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = context.getString(R.string.label_donate_screen),
                onClickBack = {
                    onUiEvent(DonateScreenUiEvent.OnClickBack)
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            Column(modifier = modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            ) {
                DonateFormContent(
                    modifier,
                    displayState.donationOption,
                    displayState.donationInput,
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
private fun DonateFormContent(
    modifier: Modifier = Modifier,
    donationOption: String,
    input: DonationInput,
    validationError: DonationInputValidationError,
    onUiEvent: (DonateScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val (changedInput, onChangeInput) = remember { mutableStateOf(input) }
    val donationOptionInfo = "You can send a donation to:<br>$donationOption<br>After that, " +
            "please save the information."

    LaunchedEffect(key1 = changedInput) {
        onUiEvent(DonateScreenUiEvent.OnChangeInput(changedInput))
    }

    Column(modifier = modifier.padding(vertical = 16.dp)) {
        HtmlText(html = donationOptionInfo)
        OutlinedTextInput(
            label = context.getString(R.string.hint_name),
            value = changedInput.name,
            isError = validationError.name.isNotEmpty(),
            supportingText = validationError.name,
        ) {
            onChangeInput(changedInput.copy(name = it))
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_email),
            value = changedInput.email,
            inputType = KeyboardType.Email,
            trailingIcon = {
                Icon(Icons.Default.Email, contentDescription = "")
            },
            isError = validationError.email.isNotEmpty(),
            supportingText = validationError.email,
        ) {
            onChangeInput(changedInput.copy(email = it))
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_reference),
            value = changedInput.reference,
            isError = validationError.reference.isNotEmpty(),
            supportingText = validationError.reference,
        ) {
            onChangeInput(changedInput.copy(reference = it))
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_message),
            value = changedInput.message,
            singleLine = false,
            minLines = 8,
            maxLines = 10,
            isError = validationError.message.isNotEmpty(),
            supportingText = validationError.message.ifEmpty {
                context.getString(R.string.helper_txt_max_length_donation_message)
            },
        ) {
            onChangeInput(changedInput.copy(message = it))
        }
        TextButton(
            onClick = { onUiEvent(DonateScreenUiEvent.OnClickSend) },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        ) {
            Text(
                context.getString(R.string.txt_send),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
        Text(text = context.getString(R.string.message_donation_thanks))
    }
}

@Composable
private fun DonateScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (DonateScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun DonateScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (DonateScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    when (this) {
        is DonateScreenState.DisplayState.MessageState.SendDonationSuccess -> {
            ShowSuccessDialog(
                message = message,
                confirmButtonText = context.getString(R.string.txt_home),
                onConfirm = {
                    onUiEvent(DonateScreenUiEvent.MessageConsumed)
                    onUiEvent(DonateScreenUiEvent.OnClickBack)
                }
            )
        }
        is DonateScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                message = message,
                onConfirm = {
                    onUiEvent(DonateScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(DonateScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}

@Composable
private fun DonateScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (DonateScreenUiEvent) -> Unit,
) {
    when (this) {
        is DonateScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
    }
    onUiEvent(DonateScreenUiEvent.NavigationConsumed)
}
