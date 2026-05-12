package com.workfort.pstuian.ui.profile.teacherprofile

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
import com.workfort.pstuian.ui.profile.teacherprofile.composable.TeacherProfileScreenContent
import com.workfort.pstuian.ui.profile.teacherprofile.state.TeacherProfileMessageState
import com.workfort.pstuian.ui.profile.teacherprofile.state.TeacherProfileNavigationState
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
fun TeacherProfileScreen(viewModel: TeacherProfileViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onUiReady()
    }

    TeacherProfileScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::messageHandled)
    HandleNavigationState(navigation, viewModel::navigationHandled)
}



@Composable
private fun HandleMessageState(
    message: TeacherProfileMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (message) {
            is TeacherProfileMessageState.Loading -> {
                ShowLoaderDialog(cancelable = message.cancelable)
            }
            is TeacherProfileMessageState.InputBio -> {
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
            is TeacherProfileMessageState.CallConfirmation -> {
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
            is TeacherProfileMessageState.EmailConfirmation -> {
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
            is TeacherProfileMessageState.ConfirmSignOut -> {
                ShowSignOutBottomSheet(
                    onConfirm = { signOutFromAllDevices ->
                        onMessageHandled()
                        message.onConfirm(signOutFromAllDevices)
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is TeacherProfileMessageState.Success -> {
                ShowSuccessDialog(
                    message = message.message,
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled,
                )
            }
            is TeacherProfileMessageState.Error -> {
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
    navigation: TeacherProfileNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(key1 = navigation) {
        navigation?.let {
            when (it) {
                is TeacherProfileNavigationState.GoBack -> navigator?.goBack()
                is TeacherProfileNavigationState.ResetToHome -> navigator?.resetTo(AppScreen.Home)
                is TeacherProfileNavigationState.ImagePreviewScreen -> {
                    navigator?.navigateToImagePreview(it.encodedImageUrl)
                }
                is TeacherProfileNavigationState.OpenUrl -> {
                    uriHandler.openUri(it.url)
                }
                is TeacherProfileNavigationState.ImageUploadScreen -> {
                    navigator?.navigateTo(AppScreen.ImageUpload(it.userId, UserType.TEACHER))
                }
                is TeacherProfileNavigationState.ChangePasswordScreen -> {
                    navigator?.navigateTo(AppScreen.ChangePassword())
                }
                is TeacherProfileNavigationState.CheckInHistoryScreen -> {
                    navigator?.navigateToCheckInHistory(it.userId, UserType.TEACHER)
                }
                is TeacherProfileNavigationState.TeacherProfileEditScreen -> {
                    navigator?.navigateToTeacherProfileEdit(it.userId)
                }
                is TeacherProfileNavigationState.DeleteAccountScreen -> {
                    navigator?.navigateTo(AppScreen.DeleteAccount)
                }
            }
            onNavigationHandled()
        }
    }
}
