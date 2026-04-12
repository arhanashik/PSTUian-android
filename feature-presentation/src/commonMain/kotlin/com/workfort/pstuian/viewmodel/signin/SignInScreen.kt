package com.workfort.pstuian.app.ui.commonmodel.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.util.isValidEmail
import com.workfort.pstuian.common.component.AppBar
import com.workfort.pstuian.common.component.HorizontalDividerWithLabel
import com.workfort.pstuian.common.component.MaterialButtonToggleGroup
import com.workfort.pstuian.common.component.OutlinedTextInput
import com.workfort.pstuian.common.component.ShowErrorDialog
import com.workfort.pstuian.common.component.ShowLoaderDialog
import com.workfort.pstuian.reducer.ui.signin.SignInScreenState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_sign_in
import pstuian.feature_presentation.generated.resources.txt_student
import pstuian.feature_presentation.generated.resources.txt_teacher
import pstuian.feature_presentation.generated.resources.hint_email
import pstuian.feature_presentation.generated.resources.hint_password
import pstuian.feature_presentation.generated.resources.txt_forgot_password
import pstuian.feature_presentation.generated.resources.txt_or
import pstuian.feature_presentation.generated.resources.hint_sign_up
import pstuian.feature_presentation.generated.resources.txt_sign_up
import pstuian.feature_presentation.generated.resources.hint_email_verification
import pstuian.feature_presentation.generated.resources.txt_verify_now

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel,
    onNavigateBack: (Boolean) -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToEmailVerification: () -> Unit,
    showToast: (String) -> Unit,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<SignInScreenUiEvent>(SignInScreenUiEvent.None)
    }

    with(screenState) {
        displayState.Handle(
            modifier = modifier,
            onShowToast = showToast,
        ) {
            uiEvent = it
        }
        navigationState?.Handle(
            onNavigateBack = onNavigateBack,
            onNavigateToForgotPassword = onNavigateToForgotPassword,
            onNavigateToSignUp = onNavigateToSignUp,
            onNavigateToEmailVerification = onNavigateToEmailVerification,
        ) {
            uiEvent = it
        }
    }

    with(uiEvent) {
        when (this) {
            is SignInScreenUiEvent.None -> Unit
            is SignInScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is SignInScreenUiEvent.OnClickUserTypeBtn -> viewModel.onClickUserTypeBtn(userType)
            is SignInScreenUiEvent.OnClickSignIn -> viewModel.signIn(email, password)
            is SignInScreenUiEvent.OnClickForgotPassword -> viewModel.onClickForgotPassword()
            is SignInScreenUiEvent.OnClickSignUp -> viewModel.onClickSignUp()
            is SignInScreenUiEvent.OnClickEmailVerification -> viewModel.onClickEmailVerification()
            is SignInScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is SignInScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = SignInScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    displayState: SignInScreenState.DisplayState,
    onUiEvent: (SignInScreenUiEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = stringResource(Res.string.txt_sign_in),
                onClickBack = {
                    onUiEvent(SignInScreenUiEvent.OnClickBack)
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
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                SignInFormContent(Modifier, displayState.userType, onUiEvent)
                SignInFooterContent(Modifier, onUiEvent)
            }
            if (displayState.isLoading) {
                ShowLoaderDialog()
            }
        }
    }
}

@Composable
private fun SignInFormContent(
    modifier: Modifier = Modifier,
    userType: UserType?,
    onUiEvent: (SignInScreenUiEvent) -> Unit,
) {
    val userTypes = listOf(
        stringResource(Res.string.txt_student),
        stringResource(Res.string.txt_teacher),
    )
    val (email, onInputEmail) = remember { mutableStateOf("") }
    var isInitialEmailRender by remember { mutableStateOf(true) }
    var emailValidationError by remember { mutableStateOf<String?>(null) }
    val (password, onInputPassword) = remember { mutableStateOf("") }
    var isInitialPasswordRender by remember { mutableStateOf(true) }
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }
    var passwordValidationError by remember { mutableStateOf<String?>(null) }

    val selectedIndex = when (userType) {
        UserType.STUDENT -> 0
        UserType.TEACHER -> 1
        else -> 0 // Using Student Sign Up as default
    }

    LaunchedEffect(key1 = email) {
        emailValidationError = if (isInitialEmailRender) {
            null
        } else {
            if (email.isEmpty()) {
                "*Required"
            } else if (email.isValidEmail().not()) {
                "*Invalid email"
            } else {
                null
            }
        }
    }

    LaunchedEffect(key1 = password) {
        passwordValidationError = if (isInitialPasswordRender) {
            null
        } else {
            if (password.isEmpty()) {
                "*Required"
            } else if (password.length < 4) {
                "*Too short. Minimum length is 4"
            } else {
                null
            }
        }
    }

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
            }?.let { newUserType ->
                if (newUserType != userType) {
                    onUiEvent(SignInScreenUiEvent.OnClickUserTypeBtn(newUserType))
                }
            }
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_email),
            value = email,
            inputType = KeyboardType.Email,
            trailingIcon = {
                Icon(Icons.Default.Email, contentDescription = "")
            },
            isError = emailValidationError.isNullOrEmpty().not(),
            supportingText = emailValidationError,
        ) {
            isInitialEmailRender = false
            onInputEmail(it)
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_password),
            value = password,
            inputType = KeyboardType.Password,
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisibility) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (passwordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            isError = passwordValidationError.isNullOrEmpty().not(),
            supportingText = passwordValidationError,
        ) {
            isInitialPasswordRender = false
            onInputPassword(it)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(
                onClick = {
                    onUiEvent(
                        SignInScreenUiEvent.OnClickSignIn(email, password),
                    )
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            ) {
                Text(
                    stringResource(Res.string.txt_sign_in),
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }
            TextButton(
                onClick = {
                    onUiEvent(
                        SignInScreenUiEvent.OnClickForgotPassword,
                    )
                }
            ) {
                Text(
                    text = stringResource(Res.string.txt_forgot_password),
                    color = Color.Gray,
                )
            }
        }
    }
}

@Composable
private fun SignInFooterContent(
    modifier: Modifier = Modifier,
    onUiEvent: (SignInScreenUiEvent) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalDividerWithLabel(
            modifier = Modifier.padding(vertical = 16.dp),
            label = stringResource(Res.string.txt_or)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(Res.string.hint_sign_up),
                modifier = Modifier.padding(horizontal = 8.dp),
            )
            TextButton(
                onClick = {
                    onUiEvent(
                        SignInScreenUiEvent.OnClickSignUp,
                    )
                }
            ) {
                Text(
                    text = stringResource(Res.string.txt_sign_up),
                )
            }
        }
        HorizontalDividerWithLabel(
            modifier = Modifier.padding(vertical = 16.dp),
            label = stringResource(Res.string.txt_or)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(Res.string.hint_email_verification),
                modifier = Modifier.padding(horizontal = 8.dp),
            )
            TextButton(
                onClick = {
                    onUiEvent(
                        SignInScreenUiEvent.OnClickEmailVerification,
                    )
                }
            ) {
                Text(
                    text = stringResource(Res.string.txt_verify_now),
                )
            }
        }
    }
}

@Composable
private fun SignInScreenState.DisplayState.Handle(
    modifier: Modifier,
    onShowToast: (String) -> Unit,
    onUiEvent: (SignInScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    val state = messageState
    if (state != null) {
        state.Handle(onShowToast, onUiEvent)
    }
}

@Composable
private fun SignInScreenState.DisplayState.MessageState.Handle(
    onShowToast: (String) -> Unit,
    onUiEvent: (SignInScreenUiEvent) -> Unit,
) {
    when (this) {
        is SignInScreenState.DisplayState.MessageState.Success -> {
            if (showToast) {
                onShowToast(message)
            }
        }
        is SignInScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                message = message,
                onConfirm = {
                    onUiEvent(SignInScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(SignInScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}

@Composable
private fun SignInScreenState.NavigationState.Handle(
    onNavigateBack: (Boolean) -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToEmailVerification: () -> Unit,
    onUiEvent: (SignInScreenUiEvent) -> Unit,
) {
    when (this) {
        is SignInScreenState.NavigationState.GoBack -> {
            onNavigateBack(isSignedIn)
        }
        is SignInScreenState.NavigationState.GoToForgotPasswordScreen -> {
            onNavigateToForgotPassword()
        }
        is SignInScreenState.NavigationState.GoToSignUpScreen -> {
            onNavigateToSignUp()
        }
        is SignInScreenState.NavigationState.GoToEmailVerificationScreen -> {
            onNavigateToEmailVerification()
        }
    }
    onUiEvent(SignInScreenUiEvent.NavigationConsumed)
}