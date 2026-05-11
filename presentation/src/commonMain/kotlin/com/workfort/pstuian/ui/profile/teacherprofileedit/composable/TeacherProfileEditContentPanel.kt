package com.workfort.pstuian.ui.profile.teacherprofileedit.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.TeacherAcademicInfoInputError
import com.workfort.pstuian.featuredomain.model.TeacherConnectInfoInputError
import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.ui.common.composable.ActionButton
import com.workfort.pstuian.ui.common.composable.ToggleSwitch
import com.workfort.pstuian.ui.common.composable.UnderlineSelectorField
import com.workfort.pstuian.ui.profile.teacherprofileedit.state.TeacherProfileEditUiEvent
import com.workfort.pstuian.ui.profile.teacherprofileedit.state.TeacherProfileEditUiState
import com.workfort.pstuian.ui.signin.composable.AuthUnderlinedExposedDropdown
import com.workfort.pstuian.ui.signin.composable.AuthUnderlinedField
import com.workfort.pstuian.ui.signin.screendata.AuthFormFieldSpacing
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import pstuian.presentation.generated.resources.Res
import pstuian.presentation.generated.resources.blood_group
import pstuian.presentation.generated.resources.hint_address
import pstuian.presentation.generated.resources.hint_blood_group
import pstuian.presentation.generated.resources.hint_department
import pstuian.presentation.generated.resources.hint_designation
import pstuian.presentation.generated.resources.hint_email
import pstuian.presentation.generated.resources.hint_facebook
import pstuian.presentation.generated.resources.hint_faculty
import pstuian.presentation.generated.resources.hint_linked_in
import pstuian.presentation.generated.resources.hint_name
import pstuian.presentation.generated.resources.hint_phone
import pstuian.presentation.generated.resources.txt_academic
import pstuian.presentation.generated.resources.txt_connect
import pstuian.presentation.generated.resources.txt_save_changes

@Composable
fun TeacherProfileEditContentPanel(
    uiState: TeacherProfileEditUiState.Content,
    onUiEvent: (TeacherProfileEditUiEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val tabOptions = listOf(stringResource(Res.string.txt_academic), stringResource(Res.string.txt_connect))

    LaunchedEffect(pagerState.currentPage) {
        onUiEvent(TeacherProfileEditUiEvent.TabClicked(pagerState.currentPage))
    }

    LaunchedEffect(uiState.selectedTabIndex) {
        if (pagerState.currentPage != uiState.selectedTabIndex) {
            pagerState.animateScrollToPage(uiState.selectedTabIndex)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ToggleSwitch(
            options = tabOptions,
            selectedIndex = uiState.selectedTabIndex,
            onSelectedIndexChange = { index ->
                scope.launch { pagerState.animateScrollToPage(index) }
            },
        )

        Spacer(Modifier.height(16.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
        ) { page ->
            when (page) {
                0 -> AcademicInfoEditPanel(
                    profile = uiState.profile,
                    validationError = uiState.academicInfoInputError,
                    onUiEvent = onUiEvent,
                )
                1 -> ConnectInfoEditPanel(
                    profile = uiState.profile,
                    validationError = uiState.connectInfoInputError,
                    onUiEvent = onUiEvent,
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        ActionButton(
            label = stringResource(Res.string.txt_save_changes).uppercase(),
            icon = Icons.AutoMirrored.Filled.ArrowForward,
            onClick = {
                if (uiState.selectedTabIndex == 0) {
                    onUiEvent(TeacherProfileEditUiEvent.AcademicInfoSaveClicked)
                } else {
                    onUiEvent(TeacherProfileEditUiEvent.ConnectInfoSaveClicked)
                }
            },
        )
    }
}

@Composable
private fun AcademicInfoEditPanel(
    profile: UserProfile.TeacherProfile,
    validationError: TeacherAcademicInfoInputError,
    onUiEvent: (TeacherProfileEditUiEvent) -> Unit,
) {
    Column {
        AuthUnderlinedField(
            label = stringResource(Res.string.hint_name),
            value = profile.teacher.name,
            onValueChange = {
                val newProfile = profile.copy(teacher = profile.teacher.copy(name = it))
                onUiEvent(TeacherProfileEditUiEvent.ProfileInfoChanged(newProfile))
            },
            isError = validationError.name.isNotEmpty(),
            supportingText = validationError.name.takeIf { it.isNotEmpty() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
        )
        Spacer(Modifier.height(AuthFormFieldSpacing))
        AuthUnderlinedField(
            label = stringResource(Res.string.hint_designation),
            value = profile.teacher.designation,
            onValueChange = {
                val newProfile = profile.copy(teacher = profile.teacher.copy(designation = it))
                onUiEvent(TeacherProfileEditUiEvent.ProfileInfoChanged(newProfile))
            },
            isError = validationError.designation.isNotEmpty(),
            supportingText = validationError.designation.takeIf { it.isNotEmpty() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
        )
        Spacer(Modifier.height(AuthFormFieldSpacing))
        AuthUnderlinedField(
            label = stringResource(Res.string.hint_department),
            value = profile.teacher.department,
            onValueChange = {
                val newProfile = profile.copy(teacher = profile.teacher.copy(department = it))
                onUiEvent(TeacherProfileEditUiEvent.ProfileInfoChanged(newProfile))
            },
            isError = validationError.department.isNotEmpty(),
            supportingText = validationError.department.takeIf { it.isNotEmpty() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
        )
        Spacer(Modifier.height(AuthFormFieldSpacing))
        UnderlineSelectorField(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(Res.string.hint_faculty),
            value = profile.faculty.shortTitle,
            isError = validationError.faculty.isNotEmpty(),
            supportingText = validationError.faculty.takeIf { it.isNotEmpty() },
            trailingIcon = {
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
            },
            onClick = { onUiEvent(TeacherProfileEditUiEvent.FacultySelectionClicked) },
        )
        Spacer(Modifier.height(AuthFormFieldSpacing))
        AuthUnderlinedExposedDropdown(
            label = stringResource(Res.string.hint_blood_group),
            value = profile.teacher.blood.orEmpty(),
            items = stringArrayResource(Res.array.blood_group).toTypedArray(),
            onItemSelected = {
                val newProfile = profile.copy(teacher = profile.teacher.copy(blood = it))
                onUiEvent(TeacherProfileEditUiEvent.ProfileInfoChanged(newProfile))
            },
            isError = validationError.bloodGroup.isNotEmpty(),
            errorText = validationError.bloodGroup.takeIf { it.isNotEmpty() },
        )
    }
}

@Composable
private fun ConnectInfoEditPanel(
    profile: UserProfile.TeacherProfile,
    validationError: TeacherConnectInfoInputError,
    onUiEvent: (TeacherProfileEditUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(4.dp))
        AuthUnderlinedField(
            label = stringResource(Res.string.hint_address),
            value = profile.teacher.address.orEmpty(),
            onValueChange = {
                val newProfile = profile.copy(teacher = profile.teacher.copy(address = it))
                onUiEvent(TeacherProfileEditUiEvent.ProfileInfoChanged(newProfile))
            },
            leadingIcon = Icons.Default.LocationOn,
            isError = validationError.address.isNotEmpty(),
            supportingText = validationError.address.takeIf { it.isNotEmpty() },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )
        Spacer(Modifier.height(AuthFormFieldSpacing))
        AuthUnderlinedField(
            label = stringResource(Res.string.hint_phone),
            value = profile.teacher.phone.orEmpty(),
            onValueChange = {
                val newProfile = profile.copy(teacher = profile.teacher.copy(phone = it))
                onUiEvent(TeacherProfileEditUiEvent.ProfileInfoChanged(newProfile))
            },
            leadingIcon = Icons.Default.Phone,
            isError = validationError.phone.isNotEmpty(),
            supportingText = validationError.phone.takeIf { it.isNotEmpty() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
            ),
        )
        Spacer(Modifier.height(AuthFormFieldSpacing))
        AuthUnderlinedField(
            label = stringResource(Res.string.hint_linked_in),
            value = profile.teacher.linkedIn.orEmpty(),
            onValueChange = {
                val newProfile = profile.copy(teacher = profile.teacher.copy(linkedIn = it))
                onUiEvent(TeacherProfileEditUiEvent.ProfileInfoChanged(newProfile))
            },
            leadingIcon = Icons.Default.Public,
            isError = validationError.linkedIn.isNotEmpty(),
            supportingText = validationError.linkedIn.takeIf { it.isNotEmpty() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Next,
            ),
        )
        Spacer(Modifier.height(AuthFormFieldSpacing))
        AuthUnderlinedField(
            label = stringResource(Res.string.hint_facebook),
            value = profile.teacher.fbLink.orEmpty(),
            onValueChange = {
                val newProfile = profile.copy(teacher = profile.teacher.copy(fbLink = it))
                onUiEvent(TeacherProfileEditUiEvent.ProfileInfoChanged(newProfile))
            },
            leadingIcon = Icons.Default.Public,
            isError = validationError.facebook.isNotEmpty(),
            supportingText = validationError.facebook.takeIf { it.isNotEmpty() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done,
            ),
        )
    }
}
