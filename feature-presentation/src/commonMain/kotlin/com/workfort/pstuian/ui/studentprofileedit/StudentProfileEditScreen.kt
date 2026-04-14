package com.workfort.pstuian.app.ui.common.ui.studentprofileedit

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.studentprofileedit.state.StudentProfileEditUiEvent
import com.workfort.pstuian.ui.studentprofileedit.state.StudentProfileEditUiState
import com.workfort.pstuian.common.composable.AnimatedErrorView
import com.workfort.pstuian.common.composable.AnimatedListLoaderView
import com.workfort.pstuian.common.composable.AppBar
import com.workfort.pstuian.common.composable.DropDownMenuBox
import com.workfort.pstuian.common.composable.OutlinedTextInput
import com.workfort.pstuian.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.common.composable.ShowInfoDialog
import com.workfort.pstuian.common.composable.ShowLoaderDialog
import com.workfort.pstuian.common.composable.ShowSuccessDialog
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.blood_group
import pstuian.feature_presentation.generated.resources.hint_address
import pstuian.feature_presentation.generated.resources.hint_batch
import pstuian.feature_presentation.generated.resources.hint_blood_group
import pstuian.feature_presentation.generated.resources.hint_email
import pstuian.feature_presentation.generated.resources.hint_facebook
import pstuian.feature_presentation.generated.resources.hint_faculty
import pstuian.feature_presentation.generated.resources.hint_id
import pstuian.feature_presentation.generated.resources.hint_linked_in
import pstuian.feature_presentation.generated.resources.hint_name
import pstuian.feature_presentation.generated.resources.hint_phone
import pstuian.feature_presentation.generated.resources.hint_reg
import pstuian.feature_presentation.generated.resources.hint_session
import pstuian.feature_presentation.generated.resources.txt_cv
import pstuian.feature_presentation.generated.resources.txt_edit
import pstuian.feature_presentation.generated.resources.txt_save_changes


@Composable
fun StudentProfileEditScreen(
    modifier: Modifier = Modifier,
    screenState: StudentProfileEditUiState,
    onUiEvent: (StudentProfileEditUiEvent) -> Unit,
) {
    LaunchedEffect(key1 = null) {
        onUiEvent(StudentProfileEditUiEvent.LoadProfile)
    }

    with(screenState) {
        displayState.Handle(modifier = modifier, onUiEvent = onUiEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    modifier: Modifier,
    displayState: StudentProfileEditUiState.DisplayState,
    onUiEvent: (StudentProfileEditUiEvent) -> Unit,
) {
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
                title = stringResource(Res.string.txt_edit),
                onClickBack = {
                    onUiEvent(StudentProfileEditUiEvent.ClickBack)
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
                    text = {  Text(text = stringResource(Res.string.txt_save_changes)) },
                    onClick = {
                        onUiEvent(StudentProfileEditUiEvent.ClickSave)
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
    onUiEvent: (StudentProfileEditUiEvent) -> Unit,
) {
    var newProfile by remember { mutableStateOf(profile) }

    // this is necessary when coming back from another screen(ex. faculty picker)
    LaunchedEffect(key1 = profile) {
        if (profile.faculty != newProfile.faculty || profile.batch != newProfile.batch) {
            newProfile = profile
        }
    }

    LaunchedEffect(key1 = newProfile) {
        onUiEvent(StudentProfileEditUiEvent.ChangeProfile(newProfile))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextInput(
            label = stringResource(Res.string.hint_name),
            value = newProfile.student.name,
            isError = validationError.name.isNotEmpty(),
            supportingText = validationError.name,
        ) {
            newProfile = newProfile.copy(
                student = newProfile.student.copy(name = it),
            )
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_id),
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
            label = stringResource(Res.string.hint_reg),
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
                    label = stringResource(Res.string.hint_blood_group),
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
            items = stringArrayResource(Res.array.blood_group).toTypedArray(),
        ) {
            newProfile = newProfile.copy(
                student = newProfile.student.copy(blood = it),
            )
        }
        OutlinedTextInput(
            modifier = Modifier.onFocusChanged {
               if (it.isFocused) {
                   onUiEvent(StudentProfileEditUiEvent.ClickFaculty)
               }
            },
            label = stringResource(Res.string.hint_faculty),
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
                    onUiEvent(StudentProfileEditUiEvent.ClickBatch)
                }
            },
            label = stringResource(Res.string.hint_batch),
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
            label = stringResource(Res.string.hint_session),
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
    onUiEvent: (StudentProfileEditUiEvent) -> Unit,
) {
    var newProfile by remember { mutableStateOf(profile) }

    LaunchedEffect(key1 = newProfile) {
        onUiEvent(StudentProfileEditUiEvent.ChangeProfile(newProfile))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.End,
    ) {
        OutlinedTextInput(
            label = stringResource(Res.string.hint_address),
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
            label = stringResource(Res.string.hint_phone),
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
            label = stringResource(Res.string.hint_email),
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
            label = stringResource(Res.string.txt_cv),
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
            label = stringResource(Res.string.hint_linked_in),
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
            label = stringResource(Res.string.hint_facebook),
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
private fun StudentProfileEditUiState.DisplayState.Handle(
    modifier: Modifier,
    onUiEvent: (StudentProfileEditUiEvent) -> Unit,
) {
    ScreenContent(
        modifier = modifier,
        displayState = this,
        onUiEvent = onUiEvent,
    )
    messageState?.Handle(onUiEvent)
}

@Composable
private fun StudentProfileEditUiState.DisplayState.PanelState.Handle(
    modifier: Modifier,
    onUiEvent: (StudentProfileEditUiEvent) -> Unit,
) {
    when (this) {
        is StudentProfileEditUiState.DisplayState.PanelState.None -> Unit
        is StudentProfileEditUiState.DisplayState.PanelState.Loading -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedListLoaderView(modifier = Modifier.fillMaxWidth())
            }
        }
        is StudentProfileEditUiState.DisplayState.PanelState.Academic -> {
            AcademicInfoEditPanelView(
                modifier = modifier,
                profile = profile,
                validationError = validationError,
                onUiEvent = onUiEvent,
            )
        }
        is StudentProfileEditUiState.DisplayState.PanelState.Connect -> {
            ConnectInfoEditPanelView(
                modifier = modifier,
                profile = profile,
                validationError = validationError,
                onUiEvent = onUiEvent,
            )
        }
        is StudentProfileEditUiState.DisplayState.PanelState.Error -> {
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
private fun StudentProfileEditUiState.DisplayState.MessageState.Handle(
    onUiEvent: (StudentProfileEditUiEvent) -> Unit,
) {
    when (this) {
        is StudentProfileEditUiState.DisplayState.MessageState.Loading -> {
            ShowLoaderDialog(cancelable = cancelable)
        }
        is StudentProfileEditUiState.DisplayState.MessageState.ConfirmSave -> {
            ShowConfirmationDialog(
                message = "Are you surely want to save the changes?",
                onConfirm = {
                    onUiEvent(StudentProfileEditUiEvent.Save)
                },
                onDismiss = {
                    onUiEvent(StudentProfileEditUiEvent.MessageConsumed)
                }
            )
        }
        is StudentProfileEditUiState.DisplayState.MessageState.Success -> {
            ShowSuccessDialog(
                message = message,
                confirmButtonText = "Go Back",
                dismissButtonText = "Edit More",
                onConfirm = {
                    onUiEvent(StudentProfileEditUiEvent.MessageConsumed)
                    onUiEvent(StudentProfileEditUiEvent.ClickBack)
                },
                onDismiss = {
                    onUiEvent(StudentProfileEditUiEvent.MessageConsumed)
                }
            )
        }
        is StudentProfileEditUiState.DisplayState.MessageState.Error -> {
            ShowInfoDialog(
                message = message,
                onDismiss = {
                    onUiEvent(StudentProfileEditUiEvent.MessageConsumed)
                }
            )
        }
    }
}

