package com.workfort.pstuian.ui.signup.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.workfort.pstuian.featuredomain.model.StudentSignUpInput
import com.workfort.pstuian.featuredomain.model.StudentSignUpInputValidationError
import com.workfort.pstuian.featuredomain.model.TeacherSignUpInput
import com.workfort.pstuian.featuredomain.model.TeacherSignUpInputValidationError
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.composable.HorizontalDividerWithLabel
import com.workfort.pstuian.ui.common.composable.LoadingOverlay
import com.workfort.pstuian.ui.common.composable.MaterialButtonToggleGroup
import com.workfort.pstuian.ui.common.composable.OutlinedTextInput
import com.workfort.pstuian.ui.signup.state.SignUpUiEvent
import com.workfort.pstuian.ui.signup.state.SignUpUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.hint_batch
import pstuian.feature_presentation.generated.resources.hint_department
import pstuian.feature_presentation.generated.resources.hint_designation
import pstuian.feature_presentation.generated.resources.hint_email
import pstuian.feature_presentation.generated.resources.hint_faculty
import pstuian.feature_presentation.generated.resources.hint_id
import pstuian.feature_presentation.generated.resources.hint_name
import pstuian.feature_presentation.generated.resources.hint_password
import pstuian.feature_presentation.generated.resources.hint_reg
import pstuian.feature_presentation.generated.resources.hint_session
import pstuian.feature_presentation.generated.resources.hint_sign_in
import pstuian.feature_presentation.generated.resources.txt_and
import pstuian.feature_presentation.generated.resources.txt_or
import pstuian.feature_presentation.generated.resources.txt_privacy_policy
import pstuian.feature_presentation.generated.resources.txt_sign_in
import pstuian.feature_presentation.generated.resources.txt_sign_up
import pstuian.feature_presentation.generated.resources.txt_sign_up_conditions
import pstuian.feature_presentation.generated.resources.txt_student
import pstuian.feature_presentation.generated.resources.txt_teacher
import pstuian.feature_presentation.generated.resources.txt_terms_and_conditions

@Composable
fun SignUpContentPanel(
    uiState: SignUpUiState,
    onUiEvent: (SignUpUiEvent) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
            ) {
                SignUpFormContent(Modifier, uiState, onUiEvent)
                SignUpFooterContent(Modifier, onUiEvent)
            }
            SignUpTermsAndConditionsPrivacyPolicyContent(Modifier, onUiEvent)
        }
        if (uiState.isLoading) {
            LoadingOverlay()
        }
    }
}

@Composable
private fun SignUpFormContent(
    modifier: Modifier = Modifier,
    uiState: SignUpUiState,
    onUiEvent: (SignUpUiEvent) -> Unit,
) {
    val userTypes = listOf(
        stringResource(Res.string.txt_student),
        stringResource(Res.string.txt_teacher),
    )
    val selectedIndex = when (uiState.userType) {
        UserType.STUDENT -> 0
        UserType.TEACHER -> 1
        else -> 0
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
            }?.let { userType ->
                if (uiState.userType != userType) {
                    onUiEvent(SignUpUiEvent.UserTypeBtnClicked(userType))
                }
            }
        }
        when (uiState.userType) {
            UserType.STUDENT -> {
                SignUpStudentFormContent(
                    modifier,
                    uiState.studentSignUpInput,
                    uiState.studentSignUpInputValidationError,
                    onUiEvent,
                )
            }
            UserType.TEACHER -> {
                SignUpTeacherFormContent(
                    modifier,
                    uiState.teacherSignUpInput,
                    uiState.teacherSignUpInputValidationError,
                    onUiEvent,
                )
            }
            else -> Unit
        }
    }
}

@Composable
private fun SignUpStudentFormContent(
    modifier: Modifier = Modifier,
    signUpInput: StudentSignUpInput,
    validationError: StudentSignUpInputValidationError,
    onUiEvent: (SignUpUiEvent) -> Unit,
) {
    // name
    val (currentSignUpInput, onChangeInput) = remember { mutableStateOf(signUpInput) }
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }

    // when faculty is changed, updating the currentState as well
    LaunchedEffect(key1 = signUpInput) {
        onChangeInput(signUpInput)
    }

    LaunchedEffect(key1 = currentSignUpInput) {
        onUiEvent(SignUpUiEvent.StudentSignUpInputChanged(currentSignUpInput))
    }

    Column(modifier = modifier) {
        OutlinedTextInput(
            label = stringResource(Res.string.hint_name),
            value = currentSignUpInput.name,
            isError = validationError.name.isNotEmpty(),
            supportingText = validationError.name,
        ) {
            onChangeInput(currentSignUpInput.copy(name = it))
        }
        Row {
            OutlinedTextInput(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(end = 8.dp),
                label = stringResource(Res.string.hint_id),
                value = currentSignUpInput.id,
                inputType = KeyboardType.Number,
                isError = validationError.id.isNotEmpty(),
                supportingText = validationError.id,
            ) {
                onChangeInput(currentSignUpInput.copy(id = it))
            }
            OutlinedTextInput(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(start = 8.dp),
                label = stringResource(Res.string.hint_reg),
                value = currentSignUpInput.reg,
                inputType = KeyboardType.Number,
                prefix = "0",
                isError = validationError.reg.isNotEmpty(),
                supportingText = validationError.reg,
            ) {
                onChangeInput(currentSignUpInput.copy(reg = it))
            }
        }
        Row {
            OutlinedTextInput(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(end = 8.dp)
                    .onFocusChanged {
                        if (it.isFocused) {
                            onUiEvent(SignUpUiEvent.FacultyClicked)
                        }
                    },
                label = stringResource(Res.string.hint_faculty),
                value = signUpInput.faculty?.shortTitle.orEmpty(),
                readOnly = true,
                trailingIcon = {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "")
                },
                isError = validationError.faculty.isNotEmpty(),
                supportingText = validationError.faculty,
                onValueChange = { },
            )
            OutlinedTextInput(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(start = 8.dp),
                label = stringResource(Res.string.hint_session),
                value = currentSignUpInput.session,
                isError = validationError.session.isNotEmpty(),
                supportingText = validationError.session,
            ) {
                onChangeInput(currentSignUpInput.copy(session = it))
            }
        }
        OutlinedTextInput(
            Modifier.onFocusChanged {
                if (it.isFocused) {
                    onUiEvent(SignUpUiEvent.BatchClicked)
                }
            },
            label = stringResource(Res.string.hint_batch),
            value = signUpInput.batch?.name.orEmpty(),
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "")
            },
            isError = validationError.batch.isNotEmpty(),
            supportingText = validationError.batch,
            onValueChange = { },
        )
        OutlinedTextInput(
            label = stringResource(Res.string.hint_email),
            value = currentSignUpInput.email,
            inputType = KeyboardType.Email,
            trailingIcon = {
                Icon(Icons.Default.Email, contentDescription = "")
            },
            isError = validationError.email.isNotEmpty(),
            supportingText = validationError.email,
        ) {
            onChangeInput(currentSignUpInput.copy(email = it))
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_password),
            value = currentSignUpInput.password,
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
            isError = validationError.password.isNotEmpty(),
            supportingText = validationError.password,
        ) {
            onChangeInput(currentSignUpInput.copy(password = it))
        }
        TextButton(
            onClick = {
                onUiEvent(SignUpUiEvent.SignUpStudentClicked)
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        ) {
            Text(
                stringResource(Res.string.txt_sign_up),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}

@Composable
private fun SignUpTeacherFormContent(
    modifier: Modifier = Modifier,
    signUpInput: TeacherSignUpInput,
    validationError: TeacherSignUpInputValidationError,
    onUiEvent: (SignUpUiEvent) -> Unit,
) {
    // name
    val (currentSignUpInput, onChangeInput) = remember { mutableStateOf(signUpInput) }
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }

    // when faculty is changed, updating the currentState as well
    LaunchedEffect(key1 = signUpInput) {
        onChangeInput(signUpInput)
    }

    LaunchedEffect(key1 = currentSignUpInput) {
        onUiEvent(SignUpUiEvent.TeacherSignUpInputChanged(currentSignUpInput))
    }

    Column(modifier = modifier) {
        OutlinedTextInput(
            label = stringResource(Res.string.hint_name),
            value = currentSignUpInput.name,
            isError = validationError.name.isNotEmpty(),
            supportingText = validationError.name,
        ) {
            onChangeInput(currentSignUpInput.copy(name = it))
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_designation),
            value = currentSignUpInput.designation,
            isError = validationError.designation.isNotEmpty(),
            supportingText = validationError.designation,
        ) {
            onChangeInput(currentSignUpInput.copy(designation = it))
        }
        OutlinedTextInput(
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) {
                    onUiEvent(SignUpUiEvent.FacultyClicked)
                }
            },
            label = stringResource(Res.string.hint_faculty),
            value = currentSignUpInput.faculty?.shortTitle.orEmpty(),
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "")
            },
            isError = validationError.faculty.isNotEmpty(),
            supportingText = validationError.faculty,
            onValueChange = { },
        )
        OutlinedTextInput(
            label = stringResource(Res.string.hint_department),
            value = currentSignUpInput.department,
            isError = validationError.department.isNotEmpty(),
            supportingText = validationError.department,
        ) {
            onChangeInput(currentSignUpInput.copy(department = it))
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_email),
            value = currentSignUpInput.email,
            inputType = KeyboardType.Email,
            trailingIcon = {
                Icon(Icons.Default.Email, contentDescription = "")
            },
            isError = validationError.email.isNotEmpty(),
            supportingText = validationError.email,
        ) {
            onChangeInput(currentSignUpInput.copy(email = it))
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_password),
            value = currentSignUpInput.password,
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
            isError = validationError.password.isNotEmpty(),
            supportingText = validationError.password,
        ) {
            onChangeInput(currentSignUpInput.copy(password = it))
        }
        TextButton(
            onClick = {
                onUiEvent(
                    SignUpUiEvent.SignUpTeacherClicked,
                )
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        ) {
            Text(
                stringResource(Res.string.txt_sign_up),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}

@Composable
private fun SignUpFooterContent(
    modifier: Modifier = Modifier,
    onUiEvent: (SignUpUiEvent) -> Unit,
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
                stringResource(Res.string.hint_sign_in),
                modifier = Modifier.padding(horizontal = 8.dp),
            )
            TextButton(
                onClick = {
                    onUiEvent(
                        SignUpUiEvent.SignInClicked,
                    )
                }
            ) {
                Text(
                    text = stringResource(Res.string.txt_sign_in),
                )
            }
        }
        Spacer(modifier = Modifier.padding(bottom = 16.dp))
    }
}

@Composable
private fun SignUpTermsAndConditionsPrivacyPolicyContent(
    modifier: Modifier = Modifier,
    onUiEvent: (SignUpUiEvent) -> Unit,
) {
    Column(
        modifier = modifier.padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Text(
            stringResource(Res.string.txt_sign_up_conditions),
            fontSize = 12.sp,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.txt_terms_and_conditions),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    onUiEvent(SignUpUiEvent.TermsAndConditionsClicked)
                }
            )
            Text(
                stringResource(Res.string.txt_and),
                modifier = Modifier.padding(horizontal = 5.dp),
            )
            Text(
                text = stringResource(Res.string.txt_privacy_policy),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    onUiEvent(SignUpUiEvent.PrivacyPolicyClicked)
                }
            )
        }
    }
}
