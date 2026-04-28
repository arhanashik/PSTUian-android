package com.workfort.pstuian.ui.profile.studentprofileedit.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.StudentAcademicInfoInputError
import com.workfort.pstuian.featuredomain.model.StudentConnectInfoInputError
import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.ui.common.composable.ActionButton
import com.workfort.pstuian.ui.common.composable.ToggleSwitch
import com.workfort.pstuian.ui.common.composable.UnderlineSelectorField
import com.workfort.pstuian.ui.profile.studentprofileedit.state.StudentProfileEditUiEvent
import com.workfort.pstuian.ui.profile.studentprofileedit.state.StudentProfileEditUiState
import com.workfort.pstuian.ui.signin.composable.AuthUnderlinedExposedDropdown
import com.workfort.pstuian.ui.signin.composable.AuthUnderlinedField
import com.workfort.pstuian.ui.signin.screendata.AuthFormFieldSpacing
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.blood_group
import pstuian.feature_presentation.generated.resources.hint_address
import pstuian.feature_presentation.generated.resources.hint_batch
import pstuian.feature_presentation.generated.resources.hint_blood_group
import pstuian.feature_presentation.generated.resources.hint_facebook
import pstuian.feature_presentation.generated.resources.hint_faculty
import pstuian.feature_presentation.generated.resources.hint_id
import pstuian.feature_presentation.generated.resources.hint_linked_in
import pstuian.feature_presentation.generated.resources.hint_name
import pstuian.feature_presentation.generated.resources.hint_phone
import pstuian.feature_presentation.generated.resources.hint_reg
import pstuian.feature_presentation.generated.resources.hint_session
import pstuian.feature_presentation.generated.resources.txt_academic
import pstuian.feature_presentation.generated.resources.txt_connect
import pstuian.feature_presentation.generated.resources.txt_cv
import pstuian.feature_presentation.generated.resources.txt_save_changes

@Composable
fun StudentProfileEditContentPanel(
    uiState: StudentProfileEditUiState.Content,
    onUiEvent: (StudentProfileEditUiEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val tabOptions = listOf(stringResource(Res.string.txt_academic), stringResource(Res.string.txt_connect))

    LaunchedEffect(pagerState.currentPage) {
        onUiEvent(StudentProfileEditUiEvent.TabClicked(pagerState.currentPage))
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
                    onUiEvent(StudentProfileEditUiEvent.AcademicInfoSaveClicked)
                } else {
                    onUiEvent(StudentProfileEditUiEvent.ConnectInfoSaveClicked)
                }
            },
        )
    }
}

@Composable
private fun AcademicInfoEditPanel(
    profile: UserProfile.StudentProfile,
    validationError: StudentAcademicInfoInputError,
    onUiEvent: (StudentProfileEditUiEvent) -> Unit,
) {
    Column{
        AuthUnderlinedField(
            label = stringResource(Res.string.hint_name),
            value = profile.student.name,
            onValueChange = {
                val newProfile = profile.copy(student = profile.student.copy(name = it))
                onUiEvent(StudentProfileEditUiEvent.ProfileInfoChanged(newProfile))
            },
            isError = validationError.name.isNotEmpty(),
            supportingText = validationError.name.takeIf { it.isNotEmpty() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
        )
        Spacer(Modifier.height(AuthFormFieldSpacing))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AuthUnderlinedField(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.hint_id),
                value = profile.student.studentId.toString(),
                onValueChange = { raw ->
                    raw.ifEmpty { "0" }.toIntOrNull()?.let { newId ->
                        val newProfile = profile.copy(student = profile.student.copy(studentId = newId))
                        onUiEvent(StudentProfileEditUiEvent.ProfileInfoChanged(newProfile))
                    }
                },
                isError = validationError.id.isNotEmpty(),
                supportingText = validationError.id.takeIf { it.isNotEmpty() },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
            )
            Spacer(modifier = Modifier.width(16.dp))
            AuthUnderlinedField(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.hint_session),
                value = profile.student.session,
                onValueChange = {
                    val newProfile = profile.copy(student = profile.student.copy(session = it))
                    onUiEvent(StudentProfileEditUiEvent.ProfileInfoChanged(newProfile))
                },
                isError = validationError.session.isNotEmpty(),
                supportingText = validationError.session.takeIf { it.isNotEmpty() },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
            )
        }
        Spacer(Modifier.height(AuthFormFieldSpacing))
        AuthUnderlinedField(
            label = stringResource(Res.string.hint_reg),
            value = profile.student.reg.removePrefix("0"),
            leadingPrefix = "0",
            onValueChange = {
                val newProfile = profile.copy(student = profile.student.copy(reg = it))
                onUiEvent(StudentProfileEditUiEvent.ProfileInfoChanged(newProfile))
            },
            isError = validationError.reg.isNotEmpty(),
            supportingText = validationError.reg.takeIf { it.isNotEmpty() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
        )
        Spacer(Modifier.height(AuthFormFieldSpacing))
        AuthUnderlinedExposedDropdown(
            label = stringResource(Res.string.hint_blood_group),
            value = profile.student.blood.orEmpty(),
            items = stringArrayResource(Res.array.blood_group).toTypedArray(),
            onItemSelected = {
                val newProfile = profile.copy(student = profile.student.copy(blood = it))
                onUiEvent(StudentProfileEditUiEvent.ProfileInfoChanged(newProfile))
            },
            isError = validationError.bloodGroup.isNotEmpty(),
            errorText = validationError.bloodGroup.takeIf { it.isNotEmpty() },
        )
        Spacer(Modifier.height(AuthFormFieldSpacing))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            UnderlineSelectorField(
                modifier = Modifier.weight(0.4f),
                label = stringResource(Res.string.hint_faculty),
                value = profile.faculty.shortTitle,
                trailingIcon = {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                },
                onClick = { onUiEvent(StudentProfileEditUiEvent.FacultySelectionClicked) },
            )
            Spacer(modifier = Modifier.width(16.dp))
            UnderlineSelectorField(
                modifier = Modifier.weight(0.6f),
                label = stringResource(Res.string.hint_batch),
                value = profile.batch.name,
                trailingIcon = {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                },
                onClick = { onUiEvent(StudentProfileEditUiEvent.BatchSelectionClicked) },
            )
        }
    }
}

@Composable
private fun ConnectInfoEditPanel(
    profile: UserProfile.StudentProfile,
    validationError: StudentConnectInfoInputError,
    onUiEvent: (StudentProfileEditUiEvent) -> Unit,
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
            value = profile.student.address.orEmpty(),
            onValueChange = {
                val newProfile = profile.copy(student = profile.student.copy(address = it))
                onUiEvent(StudentProfileEditUiEvent.ProfileInfoChanged(newProfile))
            },
            leadingIcon = Icons.Default.LocationOn,
            isError = validationError.address.isNotEmpty(),
            supportingText = validationError.address.takeIf { it.isNotEmpty() },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )
        Spacer(Modifier.height(AuthFormFieldSpacing))
        AuthUnderlinedField(
            label = stringResource(Res.string.hint_phone),
            value = profile.student.phone.orEmpty(),
            onValueChange = {
                val newProfile = profile.copy(student = profile.student.copy(phone = it))
                onUiEvent(StudentProfileEditUiEvent.ProfileInfoChanged(newProfile))
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
            label = stringResource(Res.string.txt_cv),
            value = profile.student.cvLink.orEmpty(),
            onValueChange = {
                val newProfile = profile.copy(student = profile.student.copy(cvLink = it))
                onUiEvent(StudentProfileEditUiEvent.ProfileInfoChanged(newProfile))
            },
            isError = validationError.cvLink.isNotEmpty(),
            supportingText = validationError.cvLink.takeIf { it.isNotEmpty() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Next,
            ),
        )
        Spacer(Modifier.height(AuthFormFieldSpacing))
        AuthUnderlinedField(
            label = stringResource(Res.string.hint_linked_in),
            value = profile.student.linkedIn.orEmpty(),
            onValueChange = {
                val newProfile = profile.copy(student = profile.student.copy(linkedIn = it))
                onUiEvent(StudentProfileEditUiEvent.ProfileInfoChanged(newProfile))
            },
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
            value = profile.student.fbLink.orEmpty(),
            onValueChange = {
                val newProfile = profile.copy(student = profile.student.copy(fbLink = it))
                onUiEvent(StudentProfileEditUiEvent.ProfileInfoChanged(newProfile))
            },
            isError = validationError.facebook.isNotEmpty(),
            supportingText = validationError.facebook.takeIf { it.isNotEmpty() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done,
            ),
        )
    }
}
