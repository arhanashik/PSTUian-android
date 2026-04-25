package com.workfort.pstuian.ui.profile.studentprofile.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.StudentProfile
import com.workfort.pstuian.ui.profile.studentprofile.state.StudentProfileUiEvent
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.hint_upload_new_cv
import pstuian.feature_presentation.generated.resources.txt_call
import pstuian.feature_presentation.generated.resources.txt_change_password
import pstuian.feature_presentation.generated.resources.txt_delete_account
import pstuian.feature_presentation.generated.resources.txt_email
import pstuian.feature_presentation.generated.resources.txt_go_back
import pstuian.feature_presentation.generated.resources.txt_my_check_in_list
import pstuian.feature_presentation.generated.resources.txt_my_donation_list
import pstuian.feature_presentation.generated.resources.txt_sign_out
import pstuian.feature_presentation.generated.resources.txt_signed_in_devices
import pstuian.feature_presentation.generated.resources.txt_student_profile

@Composable
internal fun StudentProfileTopBar(
    isSignedIn: Boolean,
    profile: StudentProfile?,
    onUiEvent: (StudentProfileUiEvent) -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        TopBarCircleButton(
            icon = Icons.AutoMirrored.Filled.ArrowBackIos,
            contentDescription = stringResource(Res.string.txt_go_back),
            onClick = { onUiEvent(StudentProfileUiEvent.BackClicked) },
        )

        Text(
            text = stringResource(Res.string.txt_student_profile),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )

        Box {
            TopBarCircleButton(
                icon = Icons.Filled.MoreHoriz,
                onClick = { menuExpanded = true },
            )
            ProfileOptionsDropdown(
                expanded = menuExpanded,
                isSignedIn = isSignedIn,
                profile = profile,
                onDismiss = { menuExpanded = false },
                onUiEvent = {
                    menuExpanded = false
                    onUiEvent(it)
                },
            )
        }
    }
}

@Composable
internal fun ProfileOptionsDropdown(
    expanded: Boolean,
    isSignedIn: Boolean,
    profile: StudentProfile?,
    onDismiss: () -> Unit,
    onUiEvent: (StudentProfileUiEvent) -> Unit,
) {
    val changePasswordLabel = stringResource(Res.string.txt_change_password)
    val uploadCvLabel = stringResource(Res.string.hint_upload_new_cv)
    val bloodDonationLabel = stringResource(Res.string.txt_my_donation_list)
    val checkInLabel = stringResource(Res.string.txt_my_check_in_list)
    val devicesLabel = stringResource(Res.string.txt_signed_in_devices)
    val signOutLabel = stringResource(Res.string.txt_sign_out)
    val deleteAccountLabel = stringResource(Res.string.txt_delete_account)

    DropdownMenu(expanded = expanded, onDismissRequest = onDismiss) {
        if (isSignedIn) {
            DropdownMenuItem(
                text = { Text(changePasswordLabel) },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                onClick = { onUiEvent(StudentProfileUiEvent.ChangePasswordClicked) },
            )
            DropdownMenuItem(
                text = { Text(uploadCvLabel) },
                leadingIcon = { Icon(Icons.Filled.KeyboardArrowUp, contentDescription = null) },
                onClick = { onUiEvent(StudentProfileUiEvent.UploadCvClicked) },
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text(bloodDonationLabel) },
                leadingIcon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                onClick = { onUiEvent(StudentProfileUiEvent.MyBloodDonationListClicked) },
            )
            DropdownMenuItem(
                text = { Text(checkInLabel) },
                leadingIcon = { Icon(Icons.Filled.LocationOn, contentDescription = null) },
                onClick = { onUiEvent(StudentProfileUiEvent.MyCheckInListClicked) },
            )
            DropdownMenuItem(
                text = { Text(devicesLabel) },
                leadingIcon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                onClick = { onUiEvent(StudentProfileUiEvent.MyDeviceListClicked) },
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text(signOutLabel, color = MaterialTheme.colorScheme.secondary) },
                leadingIcon = {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                },
                onClick = { onUiEvent(StudentProfileUiEvent.SignOutClicked) },
            )
            DropdownMenuItem(
                text = { Text(deleteAccountLabel, color = MaterialTheme.colorScheme.error) },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                    )
                },
                onClick = { onUiEvent(StudentProfileUiEvent.DeleteAccountClicked) },
            )
        } else {
            if (!profile?.student?.phone.isNullOrEmpty()) {
                DropdownMenuItem(
                    text = { Text(stringResource(Res.string.txt_call)) },
                    leadingIcon = { Icon(Icons.Filled.Call, contentDescription = null) },
                    onClick = { onUiEvent(StudentProfileUiEvent.CallClicked) },
                )
            }
            if (!profile?.student?.email.isNullOrEmpty()) {
                DropdownMenuItem(
                    text = { Text(stringResource(Res.string.txt_email)) },
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                    onClick = { onUiEvent(StudentProfileUiEvent.EmailClicked) },
                )
            }
        }
    }
}

@Composable
private fun TopBarCircleButton(
    icon: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit,
) {
    FilledIconButton(
        onClick = onClick,
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(20.dp),
        )
    }
}
