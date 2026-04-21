package com.workfort.pstuian.ui.signin.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun AuthFormContent(
    panel: AuthPanel,
    email: String,
    password: String,
    name: String,
    studentId: String,
    registrationNumber: String,
    faculty: String,
    batch: String,
    rememberMe: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onStudentIdChange: (String) -> Unit,
    onRegistrationNumberChange: (String) -> Unit,
    onFacultyChange: (String) -> Unit,
    onBatchChange: (String) -> Unit,
    onRememberMeToggle: () -> Unit,
    onLogin: () -> Unit,
    onForgotPasswordTap: () -> Unit,
    onEmailVerificationTap: () -> Unit,
    onResetPasswordSubmit: () -> Unit,
    onEmailVerificationSubmit: () -> Unit,
    onSignUp: () -> Unit,
    onSwitchToSignIn: () -> Unit,
    onSwitchToSignUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val nameFocus = remember { FocusRequester() }
    val emailFocus = remember { FocusRequester() }
    val studentIdFocus = remember { FocusRequester() }
    val registrationFocus = remember { FocusRequester() }
    val facultyFocus = remember { FocusRequester() }
    val batchFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val isSignIn = panel == AuthPanel.SignIn
    val isSignUp = panel == AuthPanel.SignUp
    val isForgotPassword = panel == AuthPanel.ForgotPassword
    val isEmailVerification = panel == AuthPanel.EmailVerification
    val isCompactEmailPanel = isForgotPassword || isEmailVerification
    val emailImeAction = if (isCompactEmailPanel) ImeAction.Done else ImeAction.Next
    val emailKeyboardActions = when {
        isCompactEmailPanel -> KeyboardActions(onDone = { focusManager.clearFocus() })
        isSignUp -> KeyboardActions(onNext = { studentIdFocus.requestFocus() })
        else -> KeyboardActions(onNext = { passwordFocus.requestFocus() })
    }
    val fieldSpacing = 18.dp

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp)
                .padding(top = 32.dp, bottom = 24.dp),
        ) {
            AnimatedVisibility(visible = isSignUp) {
                Column {
                    AuthUnderlinedField(
                        label = "Name",
                        value = name,
                        onValueChange = onNameChange,
                        focusRequester = nameFocus,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                        ),
                        keyboardActions = KeyboardActions(onNext = { emailFocus.requestFocus() }),
                    )
                    Spacer(modifier = Modifier.height(fieldSpacing))
                }
            }

            AuthUnderlinedField(
                label = "Email Address",
                value = email,
                onValueChange = onEmailChange,
                focusRequester = emailFocus,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = emailImeAction,
                ),
                keyboardActions = emailKeyboardActions,
            )

            AnimatedVisibility(visible = isSignUp) {
                Column {
                    Spacer(modifier = Modifier.height(fieldSpacing))
                    AuthUnderlinedField(
                        label = "Student Id",
                        value = studentId,
                        onValueChange = onStudentIdChange,
                        focusRequester = studentIdFocus,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                        ),
                        keyboardActions = KeyboardActions(onNext = { registrationFocus.requestFocus() }),
                    )
                    Spacer(modifier = Modifier.height(fieldSpacing))
                    AuthUnderlinedField(
                        label = "Registration Number",
                        value = registrationNumber,
                        onValueChange = onRegistrationNumberChange,
                        focusRequester = registrationFocus,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                        ),
                        keyboardActions = KeyboardActions(onNext = { facultyFocus.requestFocus() }),
                    )
                    Spacer(modifier = Modifier.height(fieldSpacing))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                    ) {
                        AuthUnderlinedField(
                            label = "Faculty",
                            value = faculty,
                            onValueChange = onFacultyChange,
                            modifier = Modifier.weight(1f),
                            focusRequester = facultyFocus,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next,
                            ),
                            keyboardActions = KeyboardActions(onNext = { batchFocus.requestFocus() }),
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        AuthUnderlinedField(
                            label = "Batch",
                            value = batch,
                            onValueChange = onBatchChange,
                            modifier = Modifier.weight(1f),
                            focusRequester = batchFocus,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next,
                            ),
                            keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
                        )
                    }
                }
            }

            AnimatedVisibility(visible = !isCompactEmailPanel) {
                Column {
                    Spacer(modifier = Modifier.height(fieldSpacing))
                    AuthUnderlinedField(
                        label = "Password",
                        value = password,
                        onValueChange = onPasswordChange,
                        focusRequester = passwordFocus,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingContent = {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable { passwordVisible = !passwordVisible },
                            )
                        },
                    )
                }
            }

            AnimatedVisibility(visible = isSignIn) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    RememberMeRow(
                        rememberMe = rememberMe,
                        onRememberMeToggle = onRememberMeToggle,
                        onForgotPassword = onForgotPasswordTap,
                    )
                }
            }

            AnimatedVisibility(visible = isForgotPassword) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Enter your email and we'll send you a link to reset your password.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                    )
                }
            }

            AnimatedVisibility(visible = isEmailVerification) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Enter your email and we'll send you a verification link to confirm your account.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            AnimatedContent(
                targetState = panel,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(120))
                },
                label = "primaryButton",
            ) { currentPanel ->
                val (label, onClick) = when (currentPanel) {
                    AuthPanel.SignIn -> "LOGIN" to onLogin
                    AuthPanel.SignUp -> "SIGN UP" to onSignUp
                    AuthPanel.ForgotPassword -> "RESET PASSWORD" to onResetPasswordSubmit
                    AuthPanel.EmailVerification -> "SEND VERIFICATION EMAIL" to onEmailVerificationSubmit
                }
                PrimaryAuthButton(label = label, onClick = onClick)
            }

            Spacer(modifier = Modifier.height(28.dp))
            Crossfade(
                targetState = panel,
                animationSpec = tween(250),
                label = "bottomLink",
            ) { currentPanel ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        when (currentPanel) {
                            AuthPanel.SignIn -> AuthBottomLink(
                                prefix = "Don't have an account? ",
                                action = "SIGN UP",
                                onAction = onSwitchToSignUp,
                            )
                            AuthPanel.SignUp -> AuthBottomLink(
                                prefix = "Already have an account? ",
                                action = "LOG IN",
                                onAction = onSwitchToSignIn,
                            )
                            AuthPanel.ForgotPassword -> AuthBottomLink(
                                prefix = "Remember your password? ",
                                action = "LOG IN",
                                onAction = onSwitchToSignIn,
                            )
                            AuthPanel.EmailVerification -> AuthBottomLink(
                                prefix = "Already verified? ",
                                action = "LOG IN",
                                onAction = onSwitchToSignIn,
                            )
                        }
                    }
                    if (currentPanel == AuthPanel.SignIn) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Need to verify your email? ",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 13.sp,
                            )
                            Text(
                                text = "Verify",
                                color = MaterialTheme.colorScheme.tertiary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable(onClick = onEmailVerificationTap),
                            )
                        }
                    }
                }
            }
        }
    }
}
