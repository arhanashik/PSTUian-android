package com.workfort.pstuian.ui.teacherprofileedit.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.TeacherAcademicInfoInputError
import com.workfort.pstuian.featuredomain.model.TeacherConnectInfoInputError
import com.workfort.pstuian.featuredomain.model.TeacherProfile
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.AnimatedListLoaderView
import com.workfort.pstuian.ui.common.composable.DropDownMenuBox
import com.workfort.pstuian.ui.common.composable.OutlinedTextInput
import com.workfort.pstuian.ui.teacherprofileedit.state.TeacherProfileEditUiEvent
import com.workfort.pstuian.ui.teacherprofileedit.state.TeacherProfileEditUiState
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.blood_group
import pstuian.feature_presentation.generated.resources.hint_address
import pstuian.feature_presentation.generated.resources.hint_blood_group
import pstuian.feature_presentation.generated.resources.hint_department
import pstuian.feature_presentation.generated.resources.hint_designation
import pstuian.feature_presentation.generated.resources.hint_email
import pstuian.feature_presentation.generated.resources.hint_facebook
import pstuian.feature_presentation.generated.resources.hint_faculty
import pstuian.feature_presentation.generated.resources.hint_linked_in
import pstuian.feature_presentation.generated.resources.hint_name
import pstuian.feature_presentation.generated.resources.hint_phone

@Composable
fun TeacherProfileEditContentPanel(
    modifier: Modifier = Modifier,
    uiState: TeacherProfileEditUiState.Content,
    onUiEvent: (TeacherProfileEditUiEvent) -> Unit,
) {
    when (val panelState = uiState.panelState) {
        is TeacherProfileEditUiState.PanelState.None -> Unit
        is TeacherProfileEditUiState.PanelState.Loading -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedListLoaderView(modifier = Modifier.fillMaxWidth())
            }
        }
        is TeacherProfileEditUiState.PanelState.Academic -> {
            AcademicInfoEditPanelView(
                modifier = modifier,
                profile = panelState.profile,
                validationError = panelState.validationError,
                onUiEvent = onUiEvent,
            )
        }
        is TeacherProfileEditUiState.PanelState.Connect -> {
            ConnectInfoEditPanelView(
                modifier = modifier,
                profile = panelState.profile,
                validationError = panelState.validationError,
                onUiEvent = onUiEvent,
            )
        }
        is TeacherProfileEditUiState.PanelState.Error -> {
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
private fun AcademicInfoEditPanelView(
    modifier: Modifier,
    profile: TeacherProfile,
    validationError: TeacherAcademicInfoInputError,
    onUiEvent: (TeacherProfileEditUiEvent) -> Unit,
) {
    var newProfile by remember { mutableStateOf(profile) }

    // this is necessary when coming back from another screen(ex. faculty picker)
    LaunchedEffect(key1 = profile) {
        if (profile.faculty != newProfile.faculty) {
            newProfile = profile
        }
    }

    LaunchedEffect(key1 = newProfile) {
        onUiEvent(TeacherProfileEditUiEvent.ChangeProfile(newProfile))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextInput(
            label = stringResource(Res.string.hint_name),
            value = newProfile.teacher.name,
            isError = validationError.name.isNotEmpty(),
            supportingText = validationError.name,
        ) {
            newProfile = newProfile.copy(
                teacher = newProfile.teacher.copy(name = it),
            )
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_designation),
            value = newProfile.teacher.designation,
            isError = validationError.designation.isNotEmpty(),
            supportingText = validationError.designation,
        ) {
            newProfile = newProfile.copy(
                teacher = newProfile.teacher.copy(designation = it),
            )
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_department),
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
                    label = stringResource(Res.string.hint_blood_group),
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
            items = stringArrayResource(Res.array.blood_group).toTypedArray(),
        ) {
            newProfile = newProfile.copy(
                teacher = newProfile.teacher.copy(blood = it),
            )
        }
        OutlinedTextInput(
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) {
                    onUiEvent(TeacherProfileEditUiEvent.ClickFaculty)
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
    }
}

@Composable
private fun ConnectInfoEditPanelView(
    modifier: Modifier,
    profile: TeacherProfile,
    validationError: TeacherConnectInfoInputError,
    onUiEvent: (TeacherProfileEditUiEvent) -> Unit,
) {
    var newProfile by remember { mutableStateOf(profile) }

    LaunchedEffect(key1 = newProfile) {
        onUiEvent(TeacherProfileEditUiEvent.ChangeProfile(newProfile))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextInput(
            label = stringResource(Res.string.hint_address),
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
            label = stringResource(Res.string.hint_phone),
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
            label = stringResource(Res.string.hint_email),
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
            label = stringResource(Res.string.hint_linked_in),
            value = newProfile.teacher.linkedIn.orEmpty(),
            trailingIcon = {
                Icon(Icons.Default.Public, contentDescription = "")
            },
            isError = validationError.linkedIn.isNotEmpty(),
            supportingText = validationError.linkedIn,
        ) {
            newProfile = newProfile.copy(
                teacher = newProfile.teacher.copy(linkedIn = it),
            )
        }
        OutlinedTextInput(
            label = stringResource(Res.string.hint_facebook),
            value = newProfile.teacher.fbLink.orEmpty(),
            trailingIcon = {
                Icon(Icons.Default.Public, contentDescription = "")
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
