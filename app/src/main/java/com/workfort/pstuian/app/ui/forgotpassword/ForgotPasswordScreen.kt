package com.workfort.pstuian.app.ui.forgotpassword

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.common.component.AppBar
import com.workfort.pstuian.app.ui.common.component.HorizontalDividerWithLabel
import com.workfort.pstuian.app.ui.common.component.MaterialButtonToggleGroup
import com.workfort.pstuian.app.ui.common.component.OutlinedTextInput
import com.workfort.pstuian.app.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.app.ui.common.component.ShowLoaderDialog
import com.workfort.pstuian.app.ui.common.component.ShowSuccessDialog
import com.workfort.pstuian.model.UserType


@Composable
fun ForgotPasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: ForgotPasswordViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<ForgotPasswordScreenUiEvent>(ForgotPasswordScreenUiEvent.None)
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
            is ForgotPasswordScreenUiEvent.None -> Unit
            is ForgotPasswordScreenUiEvent.OnClickBack -> viewModel.onClickSignIn()
            is ForgotPasswordScreenUiEvent.OnClickUserTypeBtn ->
                viewModel.onClickUserTypeBtn(userType)
            is ForgotPasswordScreenUiEvent.OnClickSignIn -> viewModel.onClickSignIn()
            is ForgotPasswordScreenUiEvent.OnClickSendResetLink ->
                viewModel.sendPasswordResetLink(email)
            is ForgotPasswordScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is ForgotPasswordScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = ForgotPasswordScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmailVerificationScreenContent(
    modifier: Modifier = Modifier,
    displayState: ForgotPasswordScreenState.DisplayState,
    onUiEvent: (ForgotPasswordScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = context.getString(R.string.label_forgot_password_screen),
                onClickBack = {
                    onUiEvent(ForgotPasswordScreenUiEvent.OnClickBack)
                },
                elevation = 0.dp,
            )
        },
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            Column(modifier = modifier.padding(horizontal = 16.dp)) {
                EmailVerificationFormContent(modifier, displayState, onUiEvent)
                EmailVerificationFooterContent(modifier, onUiEvent)
            }
            if (displayState.isLoading) {
                ShowLoaderDialog()
            }
        }
    }
}

@Composable
private fun EmailVerificationFormContent(
    modifier: Modifier = Modifier,
    displayState: ForgotPasswordScreenState.DisplayState,
    onUiEvent: (ForgotPasswordScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val userTypes = listOf(
        context.getString(R.string.txt_student),
        context.getString(R.string.txt_teacher),
    )
    val selectedIndex = when (displayState.userType) {
        UserType.STUDENT -> 0
        UserType.TEACHER -> 1
        else -> 0 // Using Student Sign Up as default
    }

    val (changedEmail, onChangeEmail) = remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MaterialButtonToggleGroup(
            items = userTypes,
            selectedIndex = selectedIndex,
            cornerRadius = 32.dp,
        ) {
            when (it) {
                0 -> UserType.STUDENT
                1 -> UserType.TEACHER
                else -> null
            }?.let { userType ->
                if (displayState.userType != userType) {
                    onUiEvent(ForgotPasswordScreenUiEvent.OnClickUserTypeBtn(userType))
                }
            }
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_email),
            value = changedEmail,
            inputType = KeyboardType.Email,
            trailingIcon = {
                Icon(Icons.Default.Email, contentDescription = "")
            },
            isError = displayState.validationError.isNullOrEmpty().not(),
            supportingText = displayState.validationError,
        ) {
            onChangeEmail(it)
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            TextButton(
                onClick = {
                    onUiEvent(ForgotPasswordScreenUiEvent.OnClickSendResetLink(changedEmail))
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            ) {
                Text(
                    context.getString(R.string.txt_send_reset_link),
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }
        }
        Text(text = context.getString(R.string.hint_password_recovery))
    }
}

@Composable
private fun EmailVerificationFooterContent(
    modifier: Modifier = Modifier,
    onUiEvent: (ForgotPasswordScreenUiEvent) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val context = LocalContext.current
        Spacer(modifier = Modifier.padding(top = 16.dp))
        HorizontalDividerWithLabel(
            modifier = Modifier.padding(vertical = 16.dp),
            label = context.getString(R.string.txt_or)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                context.getString(R.string.hint_remembered_password),
                modifier = Modifier.padding(end = 8.dp),
            )
            TextButton(
                onClick = { onUiEvent(ForgotPasswordScreenUiEvent.OnClickSignIn) }
            ) {
                Text(text = context.getString(R.string.txt_sign_in))
            }
        }
    }
}

@Composable
private fun ForgotPasswordScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (ForgotPasswordScreenUiEvent) -> Unit,
) {
    EmailVerificationScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun ForgotPasswordScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (ForgotPasswordScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    when (this) {
        is ForgotPasswordScreenState.DisplayState.MessageState.PasswordResetLinkSentSuccess -> {
            ShowSuccessDialog(
                message = message,
                confirmButtonText = context.getString(R.string.txt_sign_in),
                cancelable = false,
                onConfirm = {
                    onUiEvent(ForgotPasswordScreenUiEvent.MessageConsumed)
                    onUiEvent(ForgotPasswordScreenUiEvent.OnClickBack)
                }
            )
        }
        is ForgotPasswordScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                message = message,
                dismissButtonText = null,
                onConfirm = {
                    onUiEvent(ForgotPasswordScreenUiEvent.MessageConsumed)
                },
                onDismiss = { /* NO OP */ }
            )
        }
    }
}

@Composable
private fun ForgotPasswordScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (ForgotPasswordScreenUiEvent) -> Unit,
) {
    when (this) {
        is ForgotPasswordScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
    }
    onUiEvent(ForgotPasswordScreenUiEvent.NavigationConsumed)
}

//@Preview
//@Composable
//private fun PreviewSignIn() {
//    AppTheme {
//        Scaffold {
//            SigInInScreenContent(modifier = Modifier.padding(it))
//        }
//    }
//}