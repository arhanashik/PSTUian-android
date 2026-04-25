package com.workfort.pstuian.ui.profile.employeeprofile.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.workfort.pstuian.ui.profile.common.state.ProfileUiEvent
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.txt_call
import pstuian.feature_presentation.generated.resources.txt_change_password
import pstuian.feature_presentation.generated.resources.txt_delete_account
import pstuian.feature_presentation.generated.resources.txt_email
import pstuian.feature_presentation.generated.resources.txt_sign_out
import pstuian.feature_presentation.generated.resources.txt_signed_in_devices

@Composable
fun EmployeeProfileOptionsDropdown(
    expanded: Boolean,
    isSignedIn: Boolean,
    onDismiss: () -> Unit,
    onUiEvent: (ProfileUiEvent) -> Unit,
) {
    val changePasswordLabel = stringResource(Res.string.txt_change_password)
    val devicesLabel = stringResource(Res.string.txt_signed_in_devices)
    val signOutLabel = stringResource(Res.string.txt_sign_out)
    val deleteAccountLabel = stringResource(Res.string.txt_delete_account)

    DropdownMenu(expanded = expanded, onDismissRequest = onDismiss) {
        if (isSignedIn) {
            DropdownMenuItem(
                text = { Text(changePasswordLabel) },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                onClick = { onUiEvent(ProfileUiEvent.ChangePasswordClicked) },
            )
            DropdownMenuItem(
                text = { Text(devicesLabel) },
                leadingIcon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                onClick = { onUiEvent(ProfileUiEvent.MyDeviceListClicked) },
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
                onClick = { onUiEvent(ProfileUiEvent.SignOutClicked) },
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
                onClick = { onUiEvent(ProfileUiEvent.DeleteAccountClicked) },
            )
        } else {
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.txt_call)) },
                leadingIcon = { Icon(Icons.Filled.Call, contentDescription = null) },
                onClick = { onUiEvent(ProfileUiEvent.CallClicked) },
            )
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.txt_email)) },
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                onClick = { onUiEvent(ProfileUiEvent.EmailClicked) },
            )
        }
    }
}
