package com.workfort.pstuian.ui.profile.employeeprofile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.composable.bottomsheet.ShowSignOutBottomSheet
import com.workfort.pstuian.ui.common.composable.dialog.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.dialog.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.dialog.ShowInputDialog
import com.workfort.pstuian.ui.common.composable.dialog.ShowLoaderDialog
import com.workfort.pstuian.ui.common.composable.dialog.ShowSuccessDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import com.workfort.pstuian.ui.profile.employeeprofile.composable.EmployeeProfileScreenContent
import com.workfort.pstuian.ui.profile.employeeprofile.state.EmployeeProfileMessageState
import com.workfort.pstuian.ui.profile.employeeprofile.state.EmployeeProfileNavigationState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.presentation.generated.resources.Res
import pstuian.presentation.generated.resources.hint_bio
import pstuian.presentation.generated.resources.txt_call
import pstuian.presentation.generated.resources.txt_change_bio
import pstuian.presentation.generated.resources.txt_email
import pstuian.presentation.generated.resources.txt_msg_call
import pstuian.presentation.generated.resources.txt_msg_email
import pstuian.presentation.generated.resources.txt_title_call
import pstuian.presentation.generated.resources.txt_title_email
import pstuian.presentation.generated.resources.txt_update

@Composable
fun EmployeeProfileScreen(viewModel: EmployeeProfileViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    EmployeeProfileScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::messageHandled)
    HandleNavigationState(navigation, viewModel::navigationHandled)
}



@Composable
private fun HandleMessageState(
    message: EmployeeProfileMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (message) {
            is EmployeeProfileMessageState.Loading -> {
                ShowLoaderDialog(cancelable = message.cancelable)
            }
            is EmployeeProfileMessageState.InputBio -> {
                ShowInputDialog(
                    title = stringResource(Res.string.txt_change_bio),
                    label = stringResource(Res.string.hint_bio),
                    input = message.currentBio,
                    singleLine = false,
                    minLines = 3,
                    maxLines = 5,
                    maxLength = 150,
                    confirmButtonText = stringResource(Res.string.txt_update),
                    onConfirm = { newBio ->
                        onMessageHandled()
                        message.onConfirm(newBio)
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is EmployeeProfileMessageState.CallConfirmation -> {
                ShowConfirmationDialog(
                    icon = Icons.Default.Call,
                    title = stringResource(Res.string.txt_title_call),
                    message = stringResource(Res.string.txt_msg_call).plus(" ${message.phoneNumber}"),
                    confirmButtonText = stringResource(Res.string.txt_call),
                    onConfirm = {
                        onMessageHandled()
                        message.onConfirm()
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is EmployeeProfileMessageState.EmailConfirmation -> {
                ShowConfirmationDialog(
                    icon = Icons.Default.Email,
                    title = stringResource(Res.string.txt_title_email),
                    message = stringResource(Res.string.txt_msg_email).plus(" ${message.email}"),
                    confirmButtonText = stringResource(Res.string.txt_email),
                    onConfirm = {
                        onMessageHandled()
                        message.onConfirm()
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is EmployeeProfileMessageState.ConfirmSignOut -> {
                ShowSignOutBottomSheet(
                    onConfirm = { signOutFromAllDevices ->
                        onMessageHandled()
                        message.onConfirm(signOutFromAllDevices)
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is EmployeeProfileMessageState.Success -> {
                ShowSuccessDialog(
                    message = message.message,
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled,
                )
            }
            is EmployeeProfileMessageState.Error -> {
                ShowErrorDialog(
                    message = message.message,
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled,
                )
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: EmployeeProfileNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(key1 = navigation) {
        navigation?.let {
            when (it) {
                is EmployeeProfileNavigationState.GoBack -> navigator?.goBack()
                is EmployeeProfileNavigationState.ResetToHome -> navigator?.resetTo(AppScreen.Home)
                is EmployeeProfileNavigationState.ImagePreviewScreen -> {
                    navigator?.navigateToImagePreview(it.encodedImageUrl)
                }
                is EmployeeProfileNavigationState.OpenUrl -> {
                    uriHandler.openUri(it.url)
                }
                is EmployeeProfileNavigationState.ImageUploadScreen -> {
                    navigator?.navigateTo(AppScreen.ImageUpload(it.userId, UserType.EMPLOYEE))
                }
                is EmployeeProfileNavigationState.ChangePasswordScreen -> {
                    navigator?.navigateTo(AppScreen.ChangePassword())
                }
                is EmployeeProfileNavigationState.EmployeeProfileEditScreen -> {
                    navigator?.navigateTo(AppScreen.EmployeeProfileEdit(it.userId))
                }
                is EmployeeProfileNavigationState.DeleteAccountScreen -> {
                    navigator?.navigateTo(AppScreen.DeleteAccount)
                }
            }
            onNavigationHandled()
        }
    }
}
