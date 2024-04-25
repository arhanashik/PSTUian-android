package com.workfort.pstuian.app.ui.signup

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.NavItem
import com.workfort.pstuian.app.ui.NavParam
import com.workfort.pstuian.app.ui.common.component.AppBar
import com.workfort.pstuian.app.ui.common.component.HorizontalDividerWithLabel
import com.workfort.pstuian.app.ui.common.component.MaterialButtonToggleGroup
import com.workfort.pstuian.app.ui.common.component.OutlinedTextInput
import com.workfort.pstuian.app.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.app.ui.common.component.ShowLoaderDialog
import com.workfort.pstuian.app.ui.common.component.ShowSuccessDialog
import com.workfort.pstuian.appconstant.Const
import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.model.StudentSignUpInput
import com.workfort.pstuian.model.StudentSignUpInputValidationError
import com.workfort.pstuian.model.TeacherSignUpInput
import com.workfort.pstuian.model.TeacherSignUpInputValidationError
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.util.helper.LinkUtil


@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<SignUpScreenUiEvent>(SignUpScreenUiEvent.None)
    }
    val linkUtil = LinkUtil(LocalContext.current)

    // check for faculty picker
    navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<Int?>(Const.Key.FACULTY, null)
        ?.collectAsState()
        ?.value
        ?.let { facultyId ->
            // check for batch picker
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.getStateFlow<Int?>(Const.Key.BATCH, null)
                ?.collectAsState()
                ?.value
                .let { batchId ->
                    uiEvent = if (batchId == null) {
                        SignUpScreenUiEvent.OnChangeFaculty(facultyId)
                    } else {
                        SignUpScreenUiEvent.OnChangeBatch(batchId)
                    }
                }
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<Int>(Const.Key.FACULTY)
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<Int>(Const.Key.BATCH)
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
            is SignUpScreenUiEvent.None -> Unit
            is SignUpScreenUiEvent.OnClickBack -> viewModel.onClickSignIn()
            is SignUpScreenUiEvent.OnClickUserTypeBtn -> viewModel.onClickUserTypeBtn(userType)
            is SignUpScreenUiEvent.OnClickSignUpStudent -> viewModel.signUpStudent()
            is SignUpScreenUiEvent.OnClickSignUpTeacher -> viewModel.signUpTeacher()
            is SignUpScreenUiEvent.OnClickFaculty -> viewModel.onClickFaculty()
            is SignUpScreenUiEvent.OnClickBatch -> viewModel.onClickBatch()
            is SignUpScreenUiEvent.OnClickSignIn -> viewModel.onClickSignIn()
            is SignUpScreenUiEvent.OnClickTermsAndConditions ->
                linkUtil.openBrowser(NetworkConst.Remote.TERMS_AND_CONDITIONS)
            is SignUpScreenUiEvent.OnClickPrivacyPolicy ->
                linkUtil.openBrowser(NetworkConst.Remote.PRIVACY_POLICY)
            is SignUpScreenUiEvent.OnChangeFaculty -> viewModel.onChangeFaculty(facultyId)
            is SignUpScreenUiEvent.OnChangeBatch -> viewModel.onChangeBatch(batchId)
            is SignUpScreenUiEvent.OnChangeStudentSignUpInput ->
                viewModel.onChangeStudentSignUpInput(signUpInput)
            is SignUpScreenUiEvent.OnChangeTeacherSignUpInput ->
                viewModel.onChangeTeacherSignUpInput(signUpInput)
            is SignUpScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is SignUpScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = SignUpScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    displayState: SignUpScreenState.DisplayState,
    onUiEvent: (SignUpScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = context.getString(R.string.txt_sign_up),
                onClickBack = {
                    onUiEvent(SignUpScreenUiEvent.OnClickBack)
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
            Column {
                Column(
                    modifier = modifier
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                ) {
                    SignUpFormContent(modifier, displayState, onUiEvent)
                    SignUpFooterContent(modifier, onUiEvent)
                }
                SignUpTermsAndConditionsPrivacyPolicyContent(modifier, onUiEvent)
            }
            if (displayState.isLoading) {
                ShowLoaderDialog()
            }
        }
    }
}

@Composable
private fun SignUpFormContent(
    modifier: Modifier = Modifier,
    displayState: SignUpScreenState.DisplayState,
    onUiEvent: (SignUpScreenUiEvent) -> Unit,
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
                    onUiEvent(SignUpScreenUiEvent.OnClickUserTypeBtn(userType))
                }
            }
        }
        when (val state = displayState.panelState) {
            is SignUpScreenState.DisplayState.PanelState.None -> Unit
            is SignUpScreenState.DisplayState.PanelState.StudentSignUp -> {
                SignUpStudentFormContent(
                    modifier,
                    state.signUpInput,
                    state.validationError,
                    onUiEvent,
                )
            }
            is SignUpScreenState.DisplayState.PanelState.TeacherSignUp -> {
                SignUpTeacherFormContent(
                    modifier,
                    state.signUpInput,
                    state.validationError,
                    onUiEvent,
                )
            }
        }
    }
}

@Composable
private fun SignUpStudentFormContent(
    modifier: Modifier = Modifier,
    signUpInput: StudentSignUpInput,
    validationError: StudentSignUpInputValidationError,
    onUiEvent: (SignUpScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    // name
    val (currentSignUpInput, onChangeInput) = remember { mutableStateOf(signUpInput) }
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }

    // when faculty is changed, updating the currentState as well
    LaunchedEffect(key1 = signUpInput) {
        onChangeInput(signUpInput)
    }

    LaunchedEffect(key1 = currentSignUpInput) {
        onUiEvent(SignUpScreenUiEvent.OnChangeStudentSignUpInput(currentSignUpInput))
    }

    Column(modifier = modifier) {
        OutlinedTextInput(
            label = context.getString(R.string.hint_name),
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
                label = context.getString(R.string.hint_id),
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
                label = context.getString(R.string.hint_reg),
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
                            onUiEvent(SignUpScreenUiEvent.OnClickFaculty)
                        }
                    },
                label = context.getString(R.string.hint_faculty),
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
                label = context.getString(R.string.hint_session),
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
                    onUiEvent(SignUpScreenUiEvent.OnClickBatch)
                }
            },
            label = context.getString(R.string.hint_batch),
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
            label = context.getString(R.string.hint_email),
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
            label = context.getString(R.string.hint_password),
            value = currentSignUpInput.password,
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
            isError = validationError.password.isNotEmpty(),
            supportingText = validationError.password,
        ) {
            onChangeInput(currentSignUpInput.copy(password = it))
        }
        TextButton(
            onClick = {
                onUiEvent(SignUpScreenUiEvent.OnClickSignUpStudent)
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        ) {
            Text(
                context.getString(R.string.txt_sign_up),
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
    onUiEvent: (SignUpScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    // name
    val (currentSignUpInput, onChangeInput) = remember { mutableStateOf(signUpInput) }
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }

    // when faculty is changed, updating the currentState as well
    LaunchedEffect(key1 = signUpInput) {
        onChangeInput(signUpInput)
    }

    LaunchedEffect(key1 = currentSignUpInput) {
        onUiEvent(SignUpScreenUiEvent.OnChangeTeacherSignUpInput(currentSignUpInput))
    }

    Column(modifier = modifier) {
        OutlinedTextInput(
            label = context.getString(R.string.hint_name),
            value = currentSignUpInput.name,
            isError = validationError.name.isNotEmpty(),
            supportingText = validationError.name,
        ) {
            onChangeInput(currentSignUpInput.copy(name = it))
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_designation),
            value = currentSignUpInput.designation,
            isError = validationError.designation.isNotEmpty(),
            supportingText = validationError.designation,
        ) {
            onChangeInput(currentSignUpInput.copy(designation = it))
        }
        OutlinedTextInput(
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) {
                    onUiEvent(SignUpScreenUiEvent.OnClickFaculty)
                }
            },
            label = context.getString(R.string.hint_faculty),
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
            label = context.getString(R.string.hint_department),
            value = currentSignUpInput.department,
            isError = validationError.department.isNotEmpty(),
            supportingText = validationError.department,
        ) {
            onChangeInput(currentSignUpInput.copy(department = it))
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_email),
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
            label = context.getString(R.string.hint_password),
            value = currentSignUpInput.password,
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
            isError = validationError.password.isNotEmpty(),
            supportingText = validationError.password,
        ) {
            onChangeInput(currentSignUpInput.copy(password = it))
        }
        TextButton(
            onClick = {
                onUiEvent(
                    SignUpScreenUiEvent.OnClickSignUpTeacher,
                )
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        ) {
            Text(
                context.getString(R.string.txt_sign_up),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}

@Composable
private fun SignUpFooterContent(
    modifier: Modifier = Modifier,
    onUiEvent: (SignUpScreenUiEvent) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val context = LocalContext.current
        HorizontalDividerWithLabel(
            modifier = Modifier.padding(vertical = 16.dp),
            label = context.getString(R.string.txt_or)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                context.getString(R.string.hint_sign_in),
                modifier = Modifier.padding(horizontal = 8.dp),
            )
            TextButton(
                onClick = {
                    onUiEvent(
                        SignUpScreenUiEvent.OnClickSignIn,
                    )
                }
            ) {
                Text(
                    text = context.getString(R.string.txt_sign_in),
                )
            }
        }
        Spacer(modifier = Modifier.padding(bottom = 16.dp))
    }
}

@Composable
private fun SignUpTermsAndConditionsPrivacyPolicyContent(
    modifier: Modifier = Modifier,
    onUiEvent: (SignUpScreenUiEvent) -> Unit,
) {
    Column(
        modifier = modifier.padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val context = LocalContext.current
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Text(
            context.getString(R.string.txt_sign_up_conditions),
            fontSize = 12.sp,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = context.getString(R.string.txt_terms_and_conditions),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    onUiEvent(SignUpScreenUiEvent.OnClickTermsAndConditions)
                }
            )
            Text(
                context.getString(R.string.txt_and),
                modifier = Modifier.padding(horizontal = 5.dp),
            )
            Text(
                text = context.getString(R.string.txt_privacy_policy),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    onUiEvent(SignUpScreenUiEvent.OnClickPrivacyPolicy)
                }
            )
        }
    }
}

@Composable
private fun SignUpScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (SignUpScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun SignUpScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (SignUpScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    when (this) {
        is SignUpScreenState.DisplayState.MessageState.SignUpSuccess -> {
            ShowSuccessDialog(
                message = context.getString(R.string.success_msg_sign_up),
                confirmButtonText = context.getString(R.string.txt_sign_in),
                cancelable = false,
                onConfirm = {
                    onUiEvent(SignUpScreenUiEvent.MessageConsumed)
                    onUiEvent(SignUpScreenUiEvent.OnClickBack)
                }
            )
        }
        is SignUpScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                message = message,
                onConfirm = {
                    onUiEvent(SignUpScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(SignUpScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}

@Composable
private fun SignUpScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (SignUpScreenUiEvent) -> Unit,
) {
    when (this) {
        is SignUpScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
        is SignUpScreenState.NavigationState.GoToFacultyPickerScreen -> {
            navController.navigate(
                NavItem.FacultyPicker.route
                    .plus("?${NavParam.MODE}=${mode.mode}")
                    .plus("&${NavParam.FACULTY_ID}=${facultyId}")
                    .plus("&${NavParam.BATCH_ID}=${batchId}")
            )
        }
    }
    onUiEvent(SignUpScreenUiEvent.NavigationConsumed)
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