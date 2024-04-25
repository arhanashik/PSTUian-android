package com.workfort.pstuian.app.ui.contactus

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.workfort.pstuian.app.ui.common.component.OutlinedTextInput
import com.workfort.pstuian.app.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.app.ui.common.component.ShowLoaderDialog
import com.workfort.pstuian.app.ui.common.component.ShowSuccessDialog
import com.workfort.pstuian.model.ContactUsInput
import com.workfort.pstuian.model.ContactUsInputValidationError


@Composable
fun ContactUsScreen(
    modifier: Modifier = Modifier,
    viewModel: ContactUsViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<ContactUsScreenUiEvent>(ContactUsScreenUiEvent.None)
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
            is ContactUsScreenUiEvent.None -> Unit
            is ContactUsScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is ContactUsScreenUiEvent.OnClickSend -> viewModel.sendInquiry()
            is ContactUsScreenUiEvent.OnChangeContactUsInput ->
                viewModel.onChangeContactUsInput(input)
            is ContactUsScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is ContactUsScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = ContactUsScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    displayState: ContactUsScreenState.DisplayState,
    onUiEvent: (ContactUsScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = context.getString(R.string.label_contact_us),
                onClickBack = {
                    onUiEvent(ContactUsScreenUiEvent.OnClickBack)
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            Column(modifier = modifier.padding(16.dp)) {
                ContactUsFormContent(
                    modifier,
                    displayState.contactUsInput,
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
private fun ContactUsFormContent(
    modifier: Modifier = Modifier,
    contactUsInput: ContactUsInput,
    validationError: ContactUsInputValidationError,
    onUiEvent: (ContactUsScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val (changedContactUsInput, onChangeInput) = remember { mutableStateOf(contactUsInput) }

    LaunchedEffect(key1 = changedContactUsInput) {
        onUiEvent(ContactUsScreenUiEvent.OnChangeContactUsInput(changedContactUsInput))
    }

    Column(modifier = modifier) {
        OutlinedTextInput(
            label = context.getString(R.string.hint_name),
            value = changedContactUsInput.name,
            isError = validationError.name.isNotEmpty(),
            supportingText = validationError.name,
        ) {
            onChangeInput(changedContactUsInput.copy(name = it))
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_email),
            value = changedContactUsInput.email,
            inputType = KeyboardType.Email,
            trailingIcon = {
                Icon(Icons.Default.Email, contentDescription = "")
            },
            isError = validationError.email.isNotEmpty(),
            supportingText = validationError.email,
        ) {
            onChangeInput(changedContactUsInput.copy(email = it))
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_message),
            value = changedContactUsInput.message,
            singleLine = false,
            minLines = 8,
            maxLines = 10,
            isError = validationError.message.isNotEmpty(),
            supportingText = validationError.message.ifEmpty {
                context.getString(R.string.helper_text_help_message_max_length)
            },
        ) {
            onChangeInput(changedContactUsInput.copy(message = it))
        }
        TextButton(
            onClick = { onUiEvent(ContactUsScreenUiEvent.OnClickSend) },
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
private fun ContactUsScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (ContactUsScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun ContactUsScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (ContactUsScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    when (this) {
        is ContactUsScreenState.DisplayState.MessageState.SendInquirySuccess -> {
            ShowSuccessDialog(
                message = message,
                confirmButtonText = context.getString(R.string.txt_home),
                onConfirm = {
                    onUiEvent(ContactUsScreenUiEvent.MessageConsumed)
                    onUiEvent(ContactUsScreenUiEvent.OnClickBack)
                },
                onDismiss = {
                    onUiEvent(ContactUsScreenUiEvent.MessageConsumed)
                },
            )
        }
        is ContactUsScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                message = message,
                onConfirm = {
                    onUiEvent(ContactUsScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(ContactUsScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}

@Composable
private fun ContactUsScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (ContactUsScreenUiEvent) -> Unit,
) {
    when (this) {
        is ContactUsScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
    }
    onUiEvent(ContactUsScreenUiEvent.NavigationConsumed)
}
