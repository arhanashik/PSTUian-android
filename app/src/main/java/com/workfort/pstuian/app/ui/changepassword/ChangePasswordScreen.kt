package com.workfort.pstuian.app.ui.changepassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
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
import com.workfort.pstuian.app.ui.common.component.AppBar
import com.workfort.pstuian.app.ui.common.component.OutlinedTextInput
import com.workfort.pstuian.app.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.app.ui.common.component.ShowLoaderDialog
import com.workfort.pstuian.app.ui.common.component.ShowSuccessDialog
import com.workfort.pstuian.model.ChangePasswordInput
import com.workfort.pstuian.model.ChangePasswordInputError


@Composable
fun ChangePasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: ChangePasswordViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<ChangePasswordScreenUiEvent>(ChangePasswordScreenUiEvent.None)
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
            is ChangePasswordScreenUiEvent.None -> Unit
            is ChangePasswordScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is ChangePasswordScreenUiEvent.OnClickSaveBtn -> viewModel.changePassword()
            is ChangePasswordScreenUiEvent.OnChangeInput -> viewModel.onChangeInput(input)
            is ChangePasswordScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is ChangePasswordScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = ChangePasswordScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SigInScreenContent(
    modifier: Modifier = Modifier,
    displayState: ChangePasswordScreenState.DisplayState,
    onUiEvent: (ChangePasswordScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = context.getString(R.string.txt_change_password),
                onClickBack = {
                    onUiEvent(ChangePasswordScreenUiEvent.OnClickBack)
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
    input: ChangePasswordInput,
    validationError: ChangePasswordInputError,
    onUiEvent: (ChangePasswordScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val (newInput, onChangeInput) = remember { mutableStateOf(input) }
    var oldPasswordVisibility: Boolean by remember { mutableStateOf(false) }
    var newPasswordVisibility: Boolean by remember { mutableStateOf(false) }
    var confirmPasswordVisibility: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = newInput) {
        onUiEvent(ChangePasswordScreenUiEvent.OnChangeInput(newInput))
    }

    Column(modifier = modifier.padding(16.dp)) {
        OutlinedTextInput(
            label = context.getString(R.string.hint_old_password),
            value = newInput.oldPassword,
            inputType = KeyboardType.Password,
            trailingIcon = {
                val (iconRes, description) = if (oldPasswordVisibility)
                    R.drawable.ic_help_fill to "Hide password"
                else
                    R.drawable.ic_help_outline to "Show password"

                IconButton(onClick = { oldPasswordVisibility = !oldPasswordVisibility }) {
                    Icon(imageVector  = ImageVector.vectorResource(id = iconRes), description)
                }
            },
            visualTransformation = if (oldPasswordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            isError = validationError.oldPassword.isNotEmpty(),
            supportingText = validationError.oldPassword,
        ) {
            onChangeInput(newInput.copy(oldPassword = it))
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_new_password),
            value = newInput.newPassword,
            inputType = KeyboardType.Password,
            trailingIcon = {
                val (iconRes, description) = if (newPasswordVisibility)
                    R.drawable.ic_help_fill to "Hide password"
                else
                    R.drawable.ic_help_outline to "Show password"

                IconButton(onClick = { newPasswordVisibility = !newPasswordVisibility }) {
                    Icon(imageVector  = ImageVector.vectorResource(id = iconRes), description)
                }
            },
            visualTransformation = if (newPasswordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            isError = validationError.newPassword.isNotEmpty(),
            supportingText = validationError.newPassword,
        ) {
            onChangeInput(newInput.copy(newPassword = it))
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_confirm_password),
            value = newInput.confirmPassword,
            inputType = KeyboardType.Password,
            trailingIcon = {
                val (iconRes, description) = if (confirmPasswordVisibility)
                    R.drawable.ic_help_fill to "Hide password"
                else
                    R.drawable.ic_help_outline to "Show password"

                IconButton(onClick = { confirmPasswordVisibility = !confirmPasswordVisibility }) {
                    Icon(imageVector  = ImageVector.vectorResource(id = iconRes), description)
                }
            },
            visualTransformation = if (confirmPasswordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            isError = validationError.confirmPassword.isNotEmpty(),
            supportingText = validationError.confirmPassword,
        ) {
            onChangeInput(newInput.copy(confirmPassword = it))
        }
        TextButton(
            onClick = {
                onUiEvent(
                    ChangePasswordScreenUiEvent.OnClickSaveBtn,
                )
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        ) {
            Text(
                context.getString(R.string.txt_change),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}

@Composable
private fun ChangePasswordScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (ChangePasswordScreenUiEvent) -> Unit,
) {
    SigInScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun ChangePasswordScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (ChangePasswordScreenUiEvent) -> Unit,
) {
    when (this) {
        is ChangePasswordScreenState.DisplayState.MessageState.Loading -> {
            ShowLoaderDialog(cancelable = cancelable)
        }
        is ChangePasswordScreenState.DisplayState.MessageState.Success -> {
            ShowSuccessDialog(
                message = message,
                onConfirm = {
                    onUiEvent(ChangePasswordScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(ChangePasswordScreenUiEvent.MessageConsumed)
                }
            )
        }
        is ChangePasswordScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                message = message,
                onConfirm = {
                    onUiEvent(ChangePasswordScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(ChangePasswordScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}

@Composable
private fun ChangePasswordScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (ChangePasswordScreenUiEvent) -> Unit,
) {
    when (this) {
        is ChangePasswordScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
    }
    onUiEvent(ChangePasswordScreenUiEvent.NavigationConsumed)
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