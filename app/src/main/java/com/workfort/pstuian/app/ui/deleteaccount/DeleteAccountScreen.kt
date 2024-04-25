package com.workfort.pstuian.app.ui.deleteaccount

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.NavItem
import com.workfort.pstuian.app.ui.common.component.AppBar
import com.workfort.pstuian.app.ui.common.component.OutlinedTextInput
import com.workfort.pstuian.app.ui.common.component.ShowConfirmationDialog
import com.workfort.pstuian.app.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.app.ui.common.component.ShowLoaderDialog
import com.workfort.pstuian.app.ui.common.component.ShowSuccessDialog
import com.workfort.pstuian.app.ui.common.component.TitleTextSmall


@Composable
fun DeleteAccountScreen(
    modifier: Modifier = Modifier,
    viewModel: DeleteAccountViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<DeleteAccountScreenUiEvent>(DeleteAccountScreenUiEvent.None)
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
            is DeleteAccountScreenUiEvent.None -> Unit
            is DeleteAccountScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is DeleteAccountScreenUiEvent.OnClickDeleteAccountBtn ->
                viewModel.onClickDeleteAccountBtn()
            is DeleteAccountScreenUiEvent.OnChangeInput -> viewModel.onChangeInput(input)
            is DeleteAccountScreenUiEvent.OnDeleteAccount -> viewModel.deleteAccount()
            is DeleteAccountScreenUiEvent.OnRequestRecovery -> viewModel.onRequestRecovery()
            is DeleteAccountScreenUiEvent.OnResetToHomeScreen -> viewModel.onResetToHomeScreen()
            is DeleteAccountScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is DeleteAccountScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = DeleteAccountScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    displayState: DeleteAccountScreenState.DisplayState,
    onUiEvent: (DeleteAccountScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = context.getString(R.string.txt_delete_account),
                onClickBack = {
                    onUiEvent(DeleteAccountScreenUiEvent.OnClickBack)
                },
                elevation = 0.dp,
            )
        },
    ) { innerPadding ->
        FormContent(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            displayState.input,
            displayState.validationError,
            onUiEvent,
        )
    }
}

@Composable
private fun FormContent(
    modifier: Modifier = Modifier,
    input: String,
    validationError: String,
    onUiEvent: (DeleteAccountScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val (newInput, onChangeInput) = remember { mutableStateOf(input) }
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = newInput) {
        onUiEvent(DeleteAccountScreenUiEvent.OnChangeInput(newInput))
    }

    Column(modifier = modifier.padding(16.dp)) {
        TitleTextSmall(text = context.getString(R.string.msg_delete_account))
        Spacer(modifier = Modifier.padding(top = 16.dp))
        OutlinedTextInput(
            label = context.getString(R.string.hint_password),
            value = newInput,
            inputType = KeyboardType.Password,
            trailingIcon = {
                val (iconRes, description) = if (passwordVisibility)
                    R.drawable.ic_help_fill to "Hide password"
                else
                    R.drawable.ic_help_outline to "Show password"

                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector  = ImageVector.vectorResource(id = iconRes), description)
                }
            },
            visualTransformation = if (passwordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            isError = validationError.isNotEmpty(),
            supportingText = validationError,
        ) {
            onChangeInput(it)
        }
        TextButton(
            onClick = {
                onUiEvent(DeleteAccountScreenUiEvent.OnClickDeleteAccountBtn)
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            enabled = validationError.isEmpty(),
        ) {
            Text(
                context.getString(R.string.txt_delete_account),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}

@Composable
private fun DeleteAccountScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (DeleteAccountScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun DeleteAccountScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (DeleteAccountScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    when (this) {
        is DeleteAccountScreenState.DisplayState.MessageState.Loading -> {
            ShowLoaderDialog(cancelable = cancelable)
        }
        is DeleteAccountScreenState.DisplayState.MessageState.ConfirmAccountDelete -> {
            ShowConfirmationDialog(
                title = context.getString(R.string.txt_delete_account),
                message = context.getString(R.string.msg_delete_account),
                onConfirm = {
                    onUiEvent(DeleteAccountScreenUiEvent.OnDeleteAccount)
                },
                onDismiss = {
                    onUiEvent(DeleteAccountScreenUiEvent.MessageConsumed)
                }
            )
        }
        is DeleteAccountScreenState.DisplayState.MessageState.Success -> {
            ShowSuccessDialog(
                message = message,
                cancelable = false,
                confirmButtonText = "Request Recovery",
                dismissButtonText = "Open Home Screen",
                onConfirm = {
                    onUiEvent(DeleteAccountScreenUiEvent.OnRequestRecovery)
                },
                onDismiss = {
                    onUiEvent(DeleteAccountScreenUiEvent.OnResetToHomeScreen)
                }
            )
        }
        is DeleteAccountScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                message = message,
                onConfirm = {
                    onUiEvent(DeleteAccountScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(DeleteAccountScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}

@Composable
private fun DeleteAccountScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (DeleteAccountScreenUiEvent) -> Unit,
) {
    when (this) {
        is DeleteAccountScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
        is DeleteAccountScreenState.NavigationState.ResetToHomeScreen -> {
            navController.navigate(NavItem.Home.route) {
                popUpTo(NavItem.Home.route) {
                    inclusive = true
                }
            }
        }
        is DeleteAccountScreenState.NavigationState.ResetToContactUsScreen -> {
            navController.navigate(NavItem.ContactUs.route) {
                popUpTo(NavItem.Home.route) {
                    inclusive = false
                }
            }
        }
    }
    onUiEvent(DeleteAccountScreenUiEvent.NavigationConsumed)
}