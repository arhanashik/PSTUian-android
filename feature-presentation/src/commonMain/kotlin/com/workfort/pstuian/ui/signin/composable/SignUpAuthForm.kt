package com.workfort.pstuian.ui.signin.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.composable.ActionButton
import com.workfort.pstuian.ui.common.composable.ToggleSwitch
import com.workfort.pstuian.ui.common.composable.UnderlineSelectorField
import com.workfort.pstuian.ui.common.composable.batchDisplayLabel
import com.workfort.pstuian.ui.signin.screendata.AuthFormFieldSpacing
import com.workfort.pstuian.ui.signin.screendata.AuthPanel
import com.workfort.pstuian.ui.signin.screendata.SignUpFormData
import com.workfort.pstuian.ui.signin.state.SignInUiEvent
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.hint_batch
import pstuian.feature_presentation.generated.resources.hint_faculty

@Composable
internal fun SignUpAuthFormContent(
    formData: SignUpFormData,
    onUiEvent: (SignInUiEvent) -> Unit,
) {
    val selectedIndex = when (formData) {
        is SignUpFormData.StudentSignUpFormData -> 0
        is SignUpFormData.TeacherSignUpFormData -> 1
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        AuthFormPanelLayout {
            ToggleSwitch(
                listOf("Student", "Teacher"),
                selectedIndex = selectedIndex,
                onSelectedIndexChange = { index ->
                    val userType = if (index == 0) UserType.STUDENT else UserType.TEACHER
                    onUiEvent(SignInUiEvent.SignUpUserTypeToggled(userType))
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedContent(
                targetState = selectedIndex,
                transitionSpec = {
                    if (selectedIndex == 1) {
                        (slideIntoContainer(
                            towards = SlideDirection.Left,
                            animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing),
                        ) + fadeIn(
                            animationSpec = tween(durationMillis = 190, delayMillis = 90, easing = FastOutSlowInEasing),
                        ))
                            .togetherWith(
                                slideOutOfContainer(
                                    towards = SlideDirection.Left,
                                    animationSpec = tween(durationMillis = 260, easing = FastOutSlowInEasing),
                                ) + fadeOut(
                                    animationSpec = tween(durationMillis = 160, easing = FastOutSlowInEasing),
                                )
                            )
                    } else {
                        (slideIntoContainer(
                            towards = SlideDirection.Right,
                            animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing),
                        ) + fadeIn(
                            animationSpec = tween(durationMillis = 190, delayMillis = 90, easing = FastOutSlowInEasing),
                        ))
                            .togetherWith(
                                slideOutOfContainer(
                                    towards = SlideDirection.Right,
                                    animationSpec = tween(durationMillis = 260, easing = FastOutSlowInEasing),
                                ) + fadeOut(
                                    animationSpec = tween(durationMillis = 160, easing = FastOutSlowInEasing),
                                )
                            )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clipToBounds(),
                contentAlignment = Alignment.TopStart,
                label = "signUpInputFieldsSwitcher",
            ) {
                when (formData) {
                    is SignUpFormData.StudentSignUpFormData -> StudentSignUpInputFields(
                        formData = formData,
                        onUiEvent = onUiEvent,
                    )
                    is SignUpFormData.TeacherSignUpFormData -> TeacherSignUpInputFields(
                        formData = formData,
                        onUiEvent = onUiEvent,
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            AuthPrivacyPolicyAndTermsLink(
                onTermsAndConditionsClick = { onUiEvent(SignInUiEvent.TermsAndConditionsClicked) },
                onPrivacyPolicyClick = { onUiEvent(SignInUiEvent.PrivacyPolicyClicked) },
            )
            Spacer(modifier = Modifier.height(24.dp))
            ActionButton("SIGN UP", Icons.AutoMirrored.Filled.ArrowForward) {
                when (formData) {
                    is SignUpFormData.StudentSignUpFormData -> onUiEvent(SignInUiEvent.StudentSignUpClicked(formData))
                    is SignUpFormData.TeacherSignUpFormData -> onUiEvent(SignInUiEvent.TeacherSignUpClicked(formData))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            AuthBottomLink(
                prefix = "Already have an account?",
                action = "LOG IN",
                onAction = { onUiEvent(SignInUiEvent.AuthPanelChanged(AuthPanel.SignIn)) },
            )
        }
    }
}

@Composable
private fun StudentSignUpInputFields(
    formData: SignUpFormData.StudentSignUpFormData,
    onUiEvent: (SignInUiEvent) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val nameFocus = remember { FocusRequester() }
    val emailFocus = remember { FocusRequester() }
    val studentIdFocus = remember { FocusRequester() }
    val registrationFocus = remember { FocusRequester() }
    val sessionFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }

    Column(modifier = Modifier.fillMaxWidth()) {
        AuthUnderlinedField(
            label = "Name",
            value = formData.name,
            onValueChange = { onUiEvent(SignInUiEvent.SignUpFormDataChanged(formData.copy(name = it))) },
            focusRequester = nameFocus,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { emailFocus.requestFocus() }),
        )
        Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
        AuthUnderlinedField(
            label = "Email Address",
            value = formData.email,
            onValueChange = { onUiEvent(SignInUiEvent.EmailChanged(it)) },
            focusRequester = emailFocus,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { studentIdFocus.requestFocus() }),
        )
        Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
        AuthUnderlinedField(
            label = "Student Id",
            value = formData.studentId,
            onValueChange = { onUiEvent(SignInUiEvent.SignUpFormDataChanged(formData.copy(studentId = it))) },
            focusRequester = studentIdFocus,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { registrationFocus.requestFocus() }),
        )
        Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AuthUnderlinedField(
                modifier = Modifier.weight(1f),
                label = "Registration Number",
                value = formData.regNumber,
                onValueChange = { onUiEvent(SignInUiEvent.SignUpFormDataChanged(formData.copy(regNumber = it))) },
                focusRequester = registrationFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { sessionFocus.requestFocus() }),
            )
            Spacer(modifier = Modifier.width(16.dp))
            AuthUnderlinedField(
                modifier = Modifier.weight(1f),
                label = "Session",
                value = formData.session,
                onValueChange = { onUiEvent(SignInUiEvent.SignUpFormDataChanged(formData.copy(session = it))) },
                focusRequester = sessionFocus,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
            )
        }
        Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            UnderlineSelectorField(
                modifier = Modifier.weight(0.4f),
                label = stringResource(Res.string.hint_faculty),
                value = formData.faculty?.let { it.shortTitle.ifBlank { it.title } }.orEmpty(),
                trailingIcon = {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                },
                onClick = { onUiEvent(SignInUiEvent.SignUpFacultyPickerClicked) },
            )
            Spacer(modifier = Modifier.width(16.dp))
            UnderlineSelectorField(
                modifier = Modifier.weight(0.6f),
                label = stringResource(Res.string.hint_batch),
                value = formData.batch?.let { batchDisplayLabel(it) }.orEmpty(),
                trailingIcon = {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                },
                onClick = { onUiEvent(SignInUiEvent.SignUpBatchPickerClicked) },
            )
        }
        Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
        AuthPasswordField(
            password = formData.password,
            onPasswordChange = { onUiEvent(SignInUiEvent.PasswordChanged(it)) },
            focusRequester = passwordFocus,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        )
    }
}

@Composable
private fun TeacherSignUpInputFields(
    formData: SignUpFormData.TeacherSignUpFormData,
    onUiEvent: (SignInUiEvent) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val nameFocus = remember { FocusRequester() }
    val emailFocus = remember { FocusRequester() }
    val departmentFocus = remember { FocusRequester() }
    val designationFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }

    Column(modifier = Modifier.fillMaxWidth()) {
        AuthUnderlinedField(
            label = "Name",
            value = formData.name,
            onValueChange = { onUiEvent(SignInUiEvent.SignUpFormDataChanged(formData.copy(name = it))) },
            focusRequester = nameFocus,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { emailFocus.requestFocus() }),
        )
        Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
        AuthUnderlinedField(
            label = "Email Address",
            value = formData.email,
            onValueChange = { onUiEvent(SignInUiEvent.EmailChanged(it)) },
            focusRequester = emailFocus,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { departmentFocus.requestFocus() }),
        )
        Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
        UnderlineSelectorField(
            label = stringResource(Res.string.hint_faculty),
            value = formData.faculty?.let { it.shortTitle.ifBlank { it.title } }.orEmpty(),
            trailingIcon = {
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
            },
            onClick = { onUiEvent(SignInUiEvent.SignUpFacultyPickerClicked) },
        )
        Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
        AuthUnderlinedField(
            label = "Department",
            value = formData.department,
            onValueChange = { onUiEvent(SignInUiEvent.SignUpFormDataChanged(formData.copy(department = it))) },
            focusRequester = departmentFocus,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { designationFocus.requestFocus() }),
        )
        Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
        AuthUnderlinedField(
            label = "Designation",
            value = formData.designation,
            onValueChange = { onUiEvent(SignInUiEvent.SignUpFormDataChanged(formData.copy(designation = it))) },
            focusRequester = designationFocus,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
        )
        Spacer(modifier = Modifier.height(AuthFormFieldSpacing))
        AuthPasswordField(
            password = formData.password,
            onPasswordChange = { onUiEvent(SignInUiEvent.PasswordChanged(it)) },
            focusRequester = passwordFocus,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        )
    }
}