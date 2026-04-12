package com.workfort.pstuian.app.ui.common.ui.donate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.model.DonationInput
import com.workfort.pstuian.model.DonationInputValidationError
import com.workfort.pstuian.reducer.ui.donate.DonateScreenState
import com.workfort.pstuian.common.component.AppBar
import com.workfort.pstuian.common.component.HtmlText
import com.workfort.pstuian.common.component.OutlinedTextInput
import com.workfort.pstuian.common.component.ShowErrorDialog
import com.workfort.pstuian.common.component.ShowLoaderDialog
import com.workfort.pstuian.common.component.ShowSuccessDialog
import com.workfort.pstuian.common.theme.btnBgDefault
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.helper_txt_max_length_donation_message
import pstuian.feature_presentation.generated.resources.hint_email
import pstuian.feature_presentation.generated.resources.hint_message
import pstuian.feature_presentation.generated.resources.hint_name
import pstuian.feature_presentation.generated.resources.hint_reference
import pstuian.feature_presentation.generated.resources.label_donate_screen
import pstuian.feature_presentation.generated.resources.message_donation_thanks
import pstuian.feature_presentation.generated.resources.txt_home
import pstuian.feature_presentation.generated.resources.txt_send


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonateScreen(
    modifier: Modifier = Modifier,
    screenState: DonateScreenState,
    onUiEvent: (DonateUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = stringResource(Res.string.label_donate_screen),
                onClickBack = {
                    onUiEvent(DonateUiEvent.OnClickBack)
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            Column(modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            ) {
                DonateFormContent(
                    modifier = Modifier,
                    displayState = screenState.displayState,
                    onUiEvent = onUiEvent,
                )
            }
            if (screenState.displayState.isLoading) {
                ShowLoaderDialog()
            }
            screenState.displayState.messageState?.Handle(onUiEvent)
        }
    }
}

@Composable
private fun DonateFormContent(
    modifier: Modifier = Modifier,
    displayState: DonateScreenState.DisplayState,
    onUiEvent: (DonateUiEvent) -> Unit,
) {
    val (changedInput, onChangeInput) = remember(displayState.donationInput) {
        mutableStateOf(displayState.donationInput)
    }
    val donationOptionInfo = "You can send a donation to:<br>${displayState.donationOption}<br>After that, " +
            "please save the information."

    LaunchedEffect(changedInput) {
        onUiEvent(DonateUiEvent.OnChangeInput(changedInput))
    }

    Column(modifier = modifier.padding(vertical = 16.dp)) {
        HtmlText(html = donationOptionInfo)
        OutlinedTextInput(
            label = stringResource(Res.string.hint_name),
            value = changedInput.name,
            isError = displayState.validationError.name.isNotEmpty(),
            supportingText = displayState.validationError.name,
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
            isError = displayState.validationError.email.isNotEmpty(),
            supportingText = displayState.validationError.email,
        ) {
            onChangeInput(changedInput.copy(email = it))
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_reference),
            value = changedInput.reference,
            isError = displayState.validationError.reference.isNotEmpty(),
            supportingText = displayState.validationError.reference,
        ) {
            onChangeInput(changedInput.copy(reference = it))
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_message),
            value = changedInput.message,
            singleLine = false,
            minLines = 8,
            maxLines = 10,
            isError = displayState.validationError.message.isNotEmpty(),
            supportingText = displayState.validationError.message.ifEmpty {
                stringResource(Res.string.helper_txt_max_length_donation_message)
            },
        ) {
            onChangeInput(changedInput.copy(message = it))
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .btnBgDefault()
                .clickable { onUiEvent(DonateUiEvent.OnClickSend) }
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

@Composable
private fun DonateScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (DonateUiEvent) -> Unit,
) {
    when (this) {
        is DonateScreenState.DisplayState.MessageState.SendDonationSuccess -> {
            ShowSuccessDialog(
                message = message,
                confirmButtonText = stringResource(Res.string.txt_home),
                onConfirm = {
                    onUiEvent(DonateUiEvent.MessageConsumed)
                    onUiEvent(DonateUiEvent.OnClickBack)
                }
            )
        }
        is DonateScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                message = message,
                onConfirm = {
                    onUiEvent(DonateUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(DonateUiEvent.MessageConsumed)
                }
            )
        }
    }
}