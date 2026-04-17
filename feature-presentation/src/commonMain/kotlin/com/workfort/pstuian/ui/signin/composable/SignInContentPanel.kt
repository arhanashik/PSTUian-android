package com.workfort.pstuian.ui.signin.composable

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.composable.HorizontalDividerWithLabel
import com.workfort.pstuian.ui.common.composable.MaterialButtonToggleGroup
import com.workfort.pstuian.ui.common.composable.OutlinedTextInput
import com.workfort.pstuian.ui.common.composable.ShowLoaderDialog
import com.workfort.pstuian.ui.signin.state.SignInUiEvent
import com.workfort.pstuian.ui.signin.state.SignInUiState
import com.workfort.pstuian.util.isValidEmail
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.hint_email
import pstuian.feature_presentation.generated.resources.hint_email_verification
import pstuian.feature_presentation.generated.resources.hint_password
import pstuian.feature_presentation.generated.resources.hint_sign_up
import pstuian.feature_presentation.generated.resources.txt_forgot_password
import pstuian.feature_presentation.generated.resources.txt_or
import pstuian.feature_presentation.generated.resources.txt_sign_in
import pstuian.feature_presentation.generated.resources.txt_sign_up
import pstuian.feature_presentation.generated.resources.txt_student
import pstuian.feature_presentation.generated.resources.txt_teacher
import pstuian.feature_presentation.generated.resources.txt_verify_now

@Composable
fun SignInContentPanel(
    modifier: Modifier = Modifier,
    uiState: SignInUiState,
    onUiEvent: (SignInUiEvent) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SignInFormContent(Modifier, uiState.userType, onUiEvent)
            SignInFooterContent(Modifier, onUiEvent)
        }
        if (uiState.isLoading) {
            ShowLoaderDialog()
        }
    }
}

@Composable
private fun SignInFormContent(
    modifier: Modifier = Modifier,
    userType: UserType?,
    onUiEvent: (SignInUiEvent) -> Unit,
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
                    onUiEvent(SignInUiEvent.UserTypeBtnClicked(newUserType))
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
                        SignInUiEvent.SignInClicked(email, password),
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
                        SignInUiEvent.ForgotPasswordClicked,
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
    onUiEvent: (SignInUiEvent) -> Unit,
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
                        SignInUiEvent.SignUpClicked,
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
                        SignInUiEvent.EmailVerificationClicked,
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
