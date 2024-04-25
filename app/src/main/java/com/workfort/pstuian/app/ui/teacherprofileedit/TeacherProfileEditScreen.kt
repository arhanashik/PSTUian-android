package com.workfort.pstuian.app.ui.teacherprofileedit

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
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
import com.workfort.pstuian.model.TeacherAcademicInfoInputError
import com.workfort.pstuian.model.TeacherConnectInfoInputError
import com.workfort.pstuian.model.TeacherProfile
import kotlinx.coroutines.delay


@Composable
fun TeacherProfileEditScreen(
    modifier: Modifier = Modifier,
    viewModel: TeacherProfileEditViewModel,
    navController: NavHostController,
) {
    val screenState by viewModel.screenState.collectAsState()
    var uiEvent by remember {
        mutableStateOf<TeacherProfileEditScreenUiEvent>(TeacherProfileEditScreenUiEvent.None)
    }

    LaunchedEffect(key1 = null) {
        uiEvent = TeacherProfileEditScreenUiEvent.OnLoadProfile
    }

    // check for faculty picker
    navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<Int?>(Const.Key.FACULTY, null)
        ?.collectAsState()
        ?.value
        ?.let { facultyId ->
            uiEvent = TeacherProfileEditScreenUiEvent.OnChangeFaculty(facultyId)
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<Int>(Const.Key.FACULTY)
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
            is TeacherProfileEditScreenUiEvent.None -> Unit
            is TeacherProfileEditScreenUiEvent.OnLoadProfile -> viewModel.loadProfile()
            is TeacherProfileEditScreenUiEvent.OnChangeProfile ->
                viewModel.onChangeProfile(newProfile)
            is TeacherProfileEditScreenUiEvent.OnClickBack -> viewModel.onClickBack()
            is TeacherProfileEditScreenUiEvent.OnClickSave -> viewModel.onClickSave()
            is TeacherProfileEditScreenUiEvent.OnClickFaculty -> viewModel.onClickFaculty()
            is TeacherProfileEditScreenUiEvent.OnChangeFaculty ->
                viewModel.onChangeFaculty(facultyId)
            is TeacherProfileEditScreenUiEvent.OnSave -> viewModel.updateProfile()
            is TeacherProfileEditScreenUiEvent.MessageConsumed -> viewModel.messageConsumed()
            is TeacherProfileEditScreenUiEvent.NavigationConsumed -> viewModel.navigationConsumed()
        }
        uiEvent = TeacherProfileEditScreenUiEvent.None
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier,
    displayState: TeacherProfileEditScreenState.DisplayState,
    onUiEvent: (TeacherProfileEditScreenUiEvent) -> Unit,
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
                    onUiEvent(TeacherProfileEditScreenUiEvent.OnClickBack)
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
                        onUiEvent(TeacherProfileEditScreenUiEvent.OnClickSave)
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
    profile: TeacherProfile,
    validationError: TeacherAcademicInfoInputError,
    onUiEvent: (TeacherProfileEditScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    var newProfile by remember { mutableStateOf(profile) }

    // this is necessary when coming back from another screen(ex. faculty picker)
    LaunchedEffect(key1 = profile) {
        if (profile.faculty != newProfile.faculty) {
            newProfile = profile
        }
    }

    LaunchedEffect(key1 = newProfile) {
        onUiEvent(TeacherProfileEditScreenUiEvent.OnChangeProfile(newProfile))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextInput(
            label = context.getString(R.string.hint_name),
            value = newProfile.teacher.name,
            isError = validationError.name.isNotEmpty(),
            supportingText = validationError.name,
        ) {
            newProfile = newProfile.copy(
                teacher = newProfile.teacher.copy(name = it),
            )
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_designation),
            value = newProfile.teacher.designation,
            isError = validationError.designation.isNotEmpty(),
            supportingText = validationError.designation,
        ) {
            newProfile = newProfile.copy(
                teacher = newProfile.teacher.copy(designation = it),
            )
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_department),
            value = newProfile.teacher.department,
            isError = validationError.department.isNotEmpty(),
            supportingText = validationError.department,
        ) {
            newProfile = newProfile.copy(
                teacher = newProfile.teacher.copy(department = it),
            )
        }
        DropDownMenuBox(
            anchorView = { modifier, expanded ->
                OutlinedTextInput(
                    modifier = modifier,
                    label = context.getString(R.string.hint_blood_group),
                    value = newProfile.teacher.blood.orEmpty(),
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
                teacher = newProfile.teacher.copy(blood = it),
            )
        }
        OutlinedTextInput(
            modifier = Modifier.onFocusChanged {
               if (it.isFocused) {
                   onUiEvent(TeacherProfileEditScreenUiEvent.OnClickFaculty)
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
    }
}

@Composable
private fun ConnectInfoEditPanelView(
    modifier: Modifier,
    profile: TeacherProfile,
    validationError: TeacherConnectInfoInputError,
    onUiEvent: (TeacherProfileEditScreenUiEvent) -> Unit,
) {
    val context = LocalContext.current
    var newProfile by remember { mutableStateOf(profile) }

    LaunchedEffect(key1 = newProfile) {
        onUiEvent(TeacherProfileEditScreenUiEvent.OnChangeProfile(newProfile))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextInput(
            label = context.getString(R.string.hint_address),
            value = newProfile.teacher.address.orEmpty(),
            trailingIcon = {
                Icon(Icons.Default.LocationOn, contentDescription = "")
            },
            isError = validationError.address.isNotEmpty(),
            supportingText = validationError.address,
        ) {
            newProfile = newProfile.copy(
                teacher = newProfile.teacher.copy(address = it),
            )
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_phone),
            value = newProfile.teacher.phone.orEmpty(),
            inputType = KeyboardType.Phone,
            trailingIcon = {
                Icon(Icons.Default.Phone, contentDescription = "")
            },
            isError = validationError.phone.isNotEmpty(),
            supportingText = validationError.phone,
        ) {
            newProfile = newProfile.copy(
                teacher = newProfile.teacher.copy(phone = it),
            )
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_email),
            value = newProfile.teacher.email.orEmpty(),
            inputType = KeyboardType.Email,
            trailingIcon = {
                Icon(Icons.Default.Email, contentDescription = "")
            },
            isError = validationError.email.isNotEmpty(),
            supportingText = validationError.email,
        ) {
            newProfile = newProfile.copy(
                teacher = newProfile.teacher.copy(email = it),
            )
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_linked_in),
            value = newProfile.teacher.linkedIn.orEmpty(),
            trailingIcon = {
                Icon(ImageVector.vectorResource(R.drawable.ic_web), contentDescription = "")
            },
            isError = validationError.linkedIn.isNotEmpty(),
            supportingText = validationError.linkedIn,
        ) {
            newProfile = newProfile.copy(
                teacher = newProfile.teacher.copy(linkedIn = it),
            )
        }
        OutlinedTextInput(
            label = context.getString(R.string.hint_facebook),
            value = newProfile.teacher.fbLink.orEmpty(),
            trailingIcon = {
                Icon(ImageVector.vectorResource(R.drawable.ic_web), contentDescription = "")
            },
            isError = validationError.facebook.isNotEmpty(),
            supportingText = validationError.facebook,
        ) {
            newProfile = newProfile.copy(
                teacher = newProfile.teacher.copy(fbLink = it),
            )
        }
    }
}

@Composable
private fun TeacherProfileEditScreenState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (TeacherProfileEditScreenUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun TeacherProfileEditScreenState.DisplayState.PanelState.Handle(
    modifier: Modifier,
    onUiEvent: (TeacherProfileEditScreenUiEvent) -> Unit,
) {
    when (this) {
        is TeacherProfileEditScreenState.DisplayState.PanelState.None -> Unit
        is TeacherProfileEditScreenState.DisplayState.PanelState.Loading -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedListLoaderView(modifier = Modifier.fillMaxWidth())
            }
        }
        is TeacherProfileEditScreenState.DisplayState.PanelState.Academic -> {
            AcademicInfoEditPanelView(
                modifier = modifier,
                profile = profile,
                validationError = validationError,
                onUiEvent = onUiEvent,
            )
        }
        is TeacherProfileEditScreenState.DisplayState.PanelState.Connect -> {
            ConnectInfoEditPanelView(
                modifier = modifier,
                profile = profile,
                validationError = validationError,
                onUiEvent = onUiEvent,
            )
        }
        is TeacherProfileEditScreenState.DisplayState.PanelState.Error -> {
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
private fun TeacherProfileEditScreenState.DisplayState.MessageState.Handle(
    onUiEvent: (TeacherProfileEditScreenUiEvent) -> Unit,
) {
    when (this) {
        is TeacherProfileEditScreenState.DisplayState.MessageState.Loading -> {
            ShowLoaderDialog(cancelable = cancelable)
        }
        is TeacherProfileEditScreenState.DisplayState.MessageState.ConfirmSave -> {
            ShowConfirmationDialog(
                message = "Are you surely want to save the changes?",
                onConfirm = {
                    onUiEvent(TeacherProfileEditScreenUiEvent.OnSave)
                },
                onDismiss = {
                    onUiEvent(TeacherProfileEditScreenUiEvent.MessageConsumed)
                }
            )
        }
        is TeacherProfileEditScreenState.DisplayState.MessageState.Success -> {
            ShowSuccessDialog(
                message = message,
                confirmButtonText = "Go Back",
                dismissButtonText = "Edit More",
                onConfirm = {
                    onUiEvent(TeacherProfileEditScreenUiEvent.MessageConsumed)
                    onUiEvent(TeacherProfileEditScreenUiEvent.OnClickBack)
                },
                onDismiss = {
                    onUiEvent(TeacherProfileEditScreenUiEvent.MessageConsumed)
                }
            )
        }
        is TeacherProfileEditScreenState.DisplayState.MessageState.Error -> {
            ShowErrorDialog(
                message = message,
                onConfirm = {
                    onUiEvent(TeacherProfileEditScreenUiEvent.MessageConsumed)
                },
                onDismiss = {
                    onUiEvent(TeacherProfileEditScreenUiEvent.MessageConsumed)
                }
            )
        }
    }
}

@Composable
private fun TeacherProfileEditScreenState.NavigationState.Handle(
    navController: NavHostController,
    onUiEvent: (TeacherProfileEditScreenUiEvent) -> Unit,
) {
    when (this) {
        is TeacherProfileEditScreenState.NavigationState.GoBack -> {
            navController.popBackStack()
        }
        is TeacherProfileEditScreenState.NavigationState.GoToFacultyPickerScreen -> {
            navController.navigate(
                NavItem.FacultyPicker.route
                    .plus("?${NavParam.MODE}=${mode.mode}")
                    .plus("&${NavParam.FACULTY_ID}=${facultyId}"),
            )
        }
    }
    onUiEvent(TeacherProfileEditScreenUiEvent.NavigationConsumed)
}