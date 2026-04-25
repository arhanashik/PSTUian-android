package com.workfort.pstuian.ui.profile.studentprofileedit.composable

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
import com.workfort.pstuian.featuredomain.model.StudentAcademicInfoInputError
import com.workfort.pstuian.featuredomain.model.StudentConnectInfoInputError
import com.workfort.pstuian.featuredomain.model.StudentProfile
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.AnimatedListLoaderView
import com.workfort.pstuian.ui.common.composable.DropDownMenuBox
import com.workfort.pstuian.ui.common.composable.OutlinedTextInput
import com.workfort.pstuian.ui.profile.studentprofileedit.state.StudentProfileEditUiEvent
import com.workfort.pstuian.ui.profile.studentprofileedit.state.StudentProfileEditUiState
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

@Composable
fun StudentProfileEditContentPanel(
    uiState: StudentProfileEditUiState.Content,
    onUiEvent: (StudentProfileEditUiEvent) -> Unit,
) {
    when (val panelState = uiState.panelState) {
        is StudentProfileEditUiState.PanelState.None -> Unit
        is StudentProfileEditUiState.PanelState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedListLoaderView(modifier = Modifier.fillMaxWidth())
            }
        }
        is StudentProfileEditUiState.PanelState.Academic -> {
            AcademicInfoEditPanelView(
                profile = panelState.profile,
                validationError = panelState.validationError,
                onUiEvent = onUiEvent,
            )
        }
        is StudentProfileEditUiState.PanelState.Connect -> {
            ConnectInfoEditPanelView(
                profile = panelState.profile,
                validationError = panelState.validationError,
                onUiEvent = onUiEvent,
            )
        }
        is StudentProfileEditUiState.PanelState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
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
        modifier = Modifier
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
            value = newProfile.student.studentId.toString(),
            inputType = KeyboardType.Number,
            isError = validationError.id.isNotEmpty(),
            supportingText = validationError.id,
        ) {
            it.ifEmpty { "0" }.toIntOrNull()?.let { newId ->
                newProfile = newProfile.copy(
                    student = newProfile.student.copy(studentId = newId),
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
    profile: StudentProfile,
    validationError: StudentConnectInfoInputError,
    onUiEvent: (StudentProfileEditUiEvent) -> Unit,
) {
    var newProfile by remember { mutableStateOf(profile) }

    LaunchedEffect(key1 = newProfile) {
        onUiEvent(StudentProfileEditUiEvent.ChangeProfile(newProfile))
    }

    Column(
        modifier = Modifier
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
