package com.workfort.pstuian.app.ui.studentprofileedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.workfort.pstuian.R
import com.workfort.pstuian.app.ui.NavItem
import com.workfort.pstuian.app.ui.NavParam
import com.workfort.pstuian.app.ui.common.component.AnimatedErrorView
import com.workfort.pstuian.app.ui.common.component.AnimatedListLoaderView
import com.workfort.pstuian.app.ui.common.component.AppBar
import com.workfort.pstuian.app.ui.common.component.DropDownMenuBox
import com.workfort.pstuian.app.ui.common.component.OutlinedTextInput
import com.workfort.pstuian.app.ui.common.component.ShowConfirmationDialog
import com.workfort.pstuian.app.ui.common.component.ShowErrorDialog
import com.workfort.pstuian.app.ui.common.component.ShowLoaderDialog
import com.workfort.pstuian.app.ui.common.component.ShowSuccessDialog
import com.workfort.pstuian.appconstant.Const
import com.workfort.pstuian.model.StudentAcademicInfoInputError
import com.workfort.pstuian.model.StudentConnectInfoInputError
import com.workfort.pstuian.model.StudentProfile
import kotlinx.coroutines.delay


@Composable
fun StudentProfileEditScreen(
    modifier: Modifier = Modifier,
    viewModel: StudentProfileEditViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<StudentProfileEditScreenUiEvent>(StudentProfileEditScreenUiEvent.None)
    }

    LaunchedEffect(key1 = null) {
        uiEvent = StudentProfileEditScreenUiEvent.OnLoadProfile
    }

    // check for faculty picker
    navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<Int?>(Const.Key.FACULTY, null)
        ?.collectAsState()
        ?.value
        ?.let { facultyId ->
            uiEvent = StudentProfileEditScreenUiEvent.OnChangeFaculty(facultyId)
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<Int>(Const.Key.FACULTY)
        }

    // check for batch picker
    navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<Int?>(Const.Key.BATCH, null)
        ?.collectAsState()
        ?.value
        ?.let { batchId ->
            uiEvent = StudentProfileEditScreenUiEvent.OnChangeBatch(batchId)
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
            is StudentProfileEditScreenUiEvent.None -> Unit
            is StudentProfileEditScreenUiEvent.OnLoadProfile -> viewModel.loadProfile()
            is StudentProfileEditScreenUiEvent.OnChangeProfile ->
                viewModel.onChangeProfile(profile)
            is StudentProfileEditScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is StudentProfileEditScreenUiEvent.OnClickSave -> viewModel.onClickSave()
            is StudentProfileEditScreenUiEvent.OnClickFaculty -> viewModel.onClickFaculty()
            is StudentProfileEditScreenUiEvent.OnClickBatch -> viewModel.onClickBatch()
            is StudentProfileEditScreenUiEvent.OnChangeFaculty ->
                viewModel.onChangeFaculty(facultyId)
            is StudentProfileEditScreenUiEvent.OnChangeBatch ->
                viewModel.onChangeBatch(batchId)
            is StudentProfileEditScreenUiEvent.OnSave -> viewModel.updateProfile()
            is StudentProfileEditScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is StudentProfileEditScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = StudentProfileEditScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier,
    displayState: StudentProfileEditScreenState.DisplayState,
    onUiEvent: (StudentProfileEditScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var fabButtonExpanded by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = null) {
        delay(1000)
        fabButtonExpanded = false
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                scrollBehavior,
                title = context.getString(R.string.txt_edit),
                onClickBack = {
                    onUiEvent(StudentProfileEditScreenUiEvent.OnClickBack)
                },
            )
        },
        floatingActionButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                ExtendedFloatingActionButton(
                    expanded = fabButtonExpanded,
                    text = {  Text(text = context.getString(R.string.txt_save_changes)) },
                    onClick = {
                        onUiEvent(StudentProfileEditScreenUiEvent.OnClickSave)
                    },
                    icon = { Icon(Icons.Outlined.CheckCircle,"") },
                    shape = CircleShape,
                )
            }
        },
    ) { innerPadding ->
        displayState.panelState.Handle(
            modifier.padding(innerPadding),
            onUiEvent,
        )
    }
}

@Composable
private fun AcademicInfoEditPanelView(
    modifier: Modifier,
    profile: StudentProfile,
    validationError: StudentAcademicInfoInputError,
    onUiEvent: (StudentProfileEditScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    var newProfile by remember { mutableStateOf(profile) }

    // this is necessary when coming back from another screen(ex. faculty picker)
    LaunchedEffect(key1 = profile) {
        if (profile.faculty != newProfile.faculty || profile.batch != newProfile.batch) {
            newProfile = profile
        }
    }

    LaunchedEffect(key1 = newProfile) {
        onUiEvent(StudentProfileEditScreenUiEvent.OnChangeProfile(newProfile))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextInput(
            label = context.getString(R.string.hint_name),
            value = newProfile.student.name,
            isError = validationError.name.isNotEmpty(),
            supportingText = validationError.name,
        ) {
            newProfile = newProfile.copy(
                student = newProfile.student.copy(name = it),
            )
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_id),
            value = if (newProfile.student.id == 0) {
                ""
            } else {
                newProfile.student.id.toString()
            },
            inputType = KeyboardType.Number,
            isError = validationError.id.isNotEmpty(),
            supportingText = validationError.id,
        ) {
            it.ifEmpty { "0" }.toIntOrNull()?.let { newId ->
                newProfile = newProfile.copy(
                    student = newProfile.student.copy(id = newId),
                )
            }
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_reg),
            value = newProfile.student.reg.removePrefix("0"),
            inputType = KeyboardType.Number,
            prefix = "0",
            isError = validationError.reg.isNotEmpty(),
            supportingText = validationError.reg,
        ) {
            newProfile = newProfile.copy(
                student = newProfile.student.copy(reg = it),
            )
        }
        DropDownMenuBox(
            anchorView = { modifier, expanded ->
                OutlinedTextInput(
                    modifier = modifier,
                    label = context.getString(R.string.hint_blood_group),
                    value = newProfile.student.blood.orEmpty(),
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            if (expanded) {
                                Icons.Default.KeyboardArrowUp
                            } else {
                                Icons.Default.KeyboardArrowDown
                            },
                            contentDescription = "",
                        )
                    },
                    isError = validationError.bloodGroup.isNotEmpty(),
                    supportingText = validationError.bloodGroup,
                    onValueChange = { }
                )
            },
            items = context.resources.getStringArray(R.array.blood_group),
        ) {
            newProfile = newProfile.copy(
                student = newProfile.student.copy(blood = it),
            )
        }
        OutlinedTextInput(
            modifier = Modifier.onFocusChanged {
               if (it.isFocused) {
                   onUiEvent(StudentProfileEditScreenUiEvent.OnClickFaculty)
               }
            },
            label = context.getString(R.string.hint_faculty),
            value = newProfile.faculty.shortTitle,
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "")
            },
            isError = validationError.faculty.isNotEmpty(),
            supportingText = validationError.faculty,
            onValueChange = { },
        )
        OutlinedTextInput(
            Modifier.onFocusChanged {
                if (it.isFocused) {
                    onUiEvent(StudentProfileEditScreenUiEvent.OnClickBatch)
                }
            },
            label = context.getString(R.string.hint_batch),
            value = newProfile.batch.name,
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "")
            },
            isError = validationError.batch.isNotEmpty(),
            supportingText = validationError.batch,
            onValueChange = { },
        )
        OutlinedTextInput(
            label = context.getString(R.string.hint_session),
            value = newProfile.student.session,
            isError = validationError.session.isNotEmpty(),
            supportingText = validationError.session,
        ) {
            newProfile = newProfile.copy(
                student = newProfile.student.copy(session = it),
            )
        }
    }
}

@Composable
private fun ConnectInfoEditPanelView(
    modifier: Modifier,
    profile: StudentProfile,
    validationError: StudentConnectInfoInputError,
    onUiEvent: (StudentProfileEditScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    var newProfile by remember { mutableStateOf(profile) }

    LaunchedEffect(key1 = newProfile) {
        onUiEvent(StudentProfileEditScreenUiEvent.OnChangeProfile(newProfile))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.End,
    ) {
        OutlinedTextInput(
            label = context.getString(R.string.hint_address),
            value = newProfile.student.address.orEmpty(),
            trailingIcon = {
                Icon(Icons.Default.LocationOn, contentDescription = "")
            },
            isError = validationError.address.isNotEmpty(),
            supportingText = validationError.address,
        ) {
            newProfile = newProfile.copy(
                student = newProfile.student.copy(address = it),
            )
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_phone),
            value = newProfile.student.phone.orEmpty(),
            inputType = KeyboardType.Phone,
            trailingIcon = {
                Icon(Icons.Default.Phone, contentDescription = "")
            },
            isError = validationError.phone.isNotEmpty(),
            supportingText = validationError.phone,
        ) {
            newProfile = newProfile.copy(
                student = newProfile.student.copy(phone = it),
            )
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_email),
            value = newProfile.student.email.orEmpty(),
            inputType = KeyboardType.Email,
            trailingIcon = {
                Icon(Icons.Default.Email, contentDescription = "")
            },
            isError = validationError.email.isNotEmpty(),
            supportingText = validationError.email,
        ) {
            newProfile = newProfile.copy(
                student = newProfile.student.copy(email = it),
            )
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_cv),
            value = newProfile.student.cvLink.orEmpty(),
            inputType = KeyboardType.Uri,
            isError = validationError.cvLink.isNotEmpty(),
            supportingText = validationError.cvLink,
        ) {
            newProfile = newProfile.copy(
                student = newProfile.student.copy(cvLink = it),
            )
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_linked_in),
            value = newProfile.student.linkedIn.orEmpty(),
            inputType = KeyboardType.Uri,
            isError = validationError.linkedIn.isNotEmpty(),
            supportingText = validationError.linkedIn,
        ) {
            newProfile = newProfile.copy(
                student = newProfile.student.copy(linkedIn = it),
            )
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_facebook),
            value = newProfile.student.fbLink.orEmpty(),
            inputType = KeyboardType.Uri,
            isError = validationError.facebook.isNotEmpty(),
            supportingText = validationError.facebook,
        ) {
            newProfile = newProfile.copy(
                student = newProfile.student.copy(fbLink = it),
            )
        }
    }
}

@Composable
private fun StudentProfileEditScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (StudentProfileEditScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun StudentProfileEditScreenState.DisplayState.PanelState.Handle(
    modifier: Modifier,
    onUiEvent: (StudentProfileEditScreenUiEvent) -> Unit,
) {
    when (this) {
        is StudentProfileEditScreenState.DisplayState.PanelState.None -> Unit
        is StudentProfileEditScreenState.DisplayState.PanelState.Loading -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedListLoaderView(modifier = Modifier.fillMaxWidth())
            }
        }
        is StudentProfileEditScreenState.DisplayState.PanelState.Academic -> {
            AcademicInfoEditPanelView(
                modifier = modifier,
                profile = profile,
                validationError = validationError,
                onUiEvent = onUiEvent,
            )
        }
        is StudentProfileEditScreenState.DisplayState.PanelState.Connect -> {
            ConnectInfoEditPanelView(
                modifier = modifier,
                profile = profile,
                validationError = validationError,
                onUiEvent = onUiEvent,
            )
        }
        is StudentProfileEditScreenState.DisplayState.PanelState.Error -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedErrorView(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun StudentProfileEditScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (StudentProfileEditScreenUiEvent) -> Unit,
) {
    when (this) {
        is StudentProfileEditScreenState.DisplayState.MessageState.Loading -> {
            ShowLoaderDialog(cancelable = cancelable)
        }
        is StudentProfileEditScreenState.DisplayState.MessageState.ConfirmSave -> {
            ShowConfirmationDialog(
                message = "Are you surely want to save the changes?",
                onConfirm = {
                    onUiEvent(StudentProfileEditScreenUiEvent.OnSave)
                },
                onDismiss = {
                    onUiEvent(StudentProfileEditScreenUiEvent.MessageConsumed)
                }
            )
        }
        is StudentProfileEditScreenState.DisplayState.MessageState.Success -> {
            ShowSuccessDialog(
                message = message,
                confirmButtonText = "Go Back",
                dismissButtonText = "Edit More",
                onConfirm = {
                    onUiEvent(StudentProfileEditScreenUiEvent.MessageConsumed)
                    onUiEvent(StudentProfileEditScreenUiEvent.OnClickBack)
                },
                onDismiss = {
                    onUiEvent(StudentProfileEditScreenUiEvent.MessageConsumed)
                }
            )
        }
        is StudentProfileEditScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                message = message,
                onConfirm = {
                    onUiEvent(StudentProfileEditScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(StudentProfileEditScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}

@Composable
private fun StudentProfileEditScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (StudentProfileEditScreenUiEvent) -> Unit,
) {
    when (this) {
        is StudentProfileEditScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
        is StudentProfileEditScreenState.NavigationState.GoToFacultyPickerScreen -> {
            navController.navigate(
                NavItem.FacultyPicker.route
                    .plus("?${NavParam.MODE}=${mode.mode}")
                    .plus("&${NavParam.FACULTY_ID}=${facultyId}")
                    .plus("&${NavParam.BATCH_ID}=${batchId}")
            )
        }
    }
    onUiEvent(StudentProfileEditScreenUiEvent.NavigationConsumed)
}