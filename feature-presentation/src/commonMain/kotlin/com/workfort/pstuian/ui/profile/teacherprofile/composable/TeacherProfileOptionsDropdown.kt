package com.workfort.pstuian.ui.profile.teacherprofile.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import com.workfort.pstuian.ui.profile.common.state.ProfileUiEvent
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_call
import pstuian.feature_presentation.generated.resources.txt_change_password
import pstuian.feature_presentation.generated.resources.txt_delete_account
import pstuian.feature_presentation.generated.resources.txt_email
import pstuian.feature_presentation.generated.resources.txt_my_check_in_list
import pstuian.feature_presentation.generated.resources.txt_sign_out
import pstuian.feature_presentation.generated.resources.txt_signed_in_devices

@Composable
fun TeacherProfileOptionsDropdown(
    expanded: Boolean,
    isSignedIn: Boolean,
    onDismiss: () -> Unit,
    onUiEvent: (ProfileUiEvent) -> Unit,
) {
    val changePasswordLabel = stringResource(Res.string.txt_change_password)
    val checkInLabel = stringResource(Res.string.txt_my_check_in_list)
    val devicesLabel = stringResource(Res.string.txt_signed_in_devices)
    val signOutLabel = stringResource(Res.string.txt_sign_out)
    val deleteAccountLabel = stringResource(Res.string.txt_delete_account)

    DropdownMenu(expanded = expanded, onDismissRequest = onDismiss) {
        if (isSignedIn) {
            DropdownMenuItem(
                text = { Text(changePasswordLabel, style = TextStyle.body2, color = AppColors.textPrimary) },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                onClick = {
                    onDismiss()
                    onUiEvent(ProfileUiEvent.ChangePasswordClicked)
                },
            )
            DropdownMenuItem(
                text = { Text(checkInLabel, style = TextStyle.body2, color = AppColors.textPrimary) },
                leadingIcon = { Icon(Icons.Filled.LocationOn, contentDescription = null) },
                onClick = {
                    onDismiss()
                    onUiEvent(ProfileUiEvent.MyCheckInListClicked)
                },
            )
            DropdownMenuItem(
                text = { Text(devicesLabel, style = TextStyle.body2, color = AppColors.textPrimary) },
                leadingIcon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                onClick = {
                    onDismiss()
                    onUiEvent(ProfileUiEvent.MyDeviceListClicked)
                },
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text(signOutLabel, style = TextStyle.body2, color = AppColors.primaryVariant) },
                leadingIcon = {
                    Icon(
                        Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = AppColors.primaryVariant,
                    )
                },
                onClick = {
                    onDismiss()
                    onUiEvent(ProfileUiEvent.SignOutClicked)
                },
            )
            DropdownMenuItem(
                text = { Text(deleteAccountLabel, style = TextStyle.body2, color = AppColors.error) },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = null,
                        tint = AppColors.error,
                    )
                },
                onClick = {
                    onDismiss()
                    onUiEvent(ProfileUiEvent.DeleteAccountClicked)
                },
            )
        } else {
            DropdownMenuItem(
                text = {
                    Text(stringResource(Res.string.txt_call), style = TextStyle.body2, color = AppColors.textPrimary)
                },
                leadingIcon = { Icon(Icons.Filled.Call, contentDescription = null) },
                onClick = {
                    onDismiss()
                    onUiEvent(ProfileUiEvent.CallClicked)
                },
            )
            DropdownMenuItem(
                text = {
                    Text(stringResource(Res.string.txt_email), style = TextStyle.body2, color = AppColors.textPrimary)
                },
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                onClick = {
                    onDismiss()
                    onUiEvent(ProfileUiEvent.EmailClicked)
                },
            )
        }
    }
}
