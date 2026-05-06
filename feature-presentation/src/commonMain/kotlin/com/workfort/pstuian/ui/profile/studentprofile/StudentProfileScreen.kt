package com.workfort.pstuian.ui.profile.studentprofile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.composable.AppSnackbarHost
import com.workfort.pstuian.ui.common.composable.HandleSnackbar
import com.workfort.pstuian.ui.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.ShowInputDialog
import com.workfort.pstuian.ui.common.composable.ShowLoaderDialog
import com.workfort.pstuian.ui.common.composable.ShowSignOutBottomSheet
import com.workfort.pstuian.ui.common.composable.ShowSuccessDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import com.workfort.pstuian.ui.cvdownload.composable.CvDownloadBottomSheet
import com.workfort.pstuian.ui.cvupload.composable.CvUploadBottomSheet
import com.workfort.pstuian.ui.profile.studentprofile.composable.StudentProfileScreenContent
import com.workfort.pstuian.ui.profile.studentprofile.state.StudentProfileMessageState
import com.workfort.pstuian.ui.profile.studentprofile.state.StudentProfileNavigationState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.hint_bio
import pstuian.feature_presentation.generated.resources.txt_call
import pstuian.feature_presentation.generated.resources.txt_change_bio
import pstuian.feature_presentation.generated.resources.txt_email
import pstuian.feature_presentation.generated.resources.txt_msg_call
import pstuian.feature_presentation.generated.resources.txt_msg_email
import pstuian.feature_presentation.generated.resources.txt_title_call
import pstuian.feature_presentation.generated.resources.txt_title_email
import pstuian.feature_presentation.generated.resources.txt_update

@Composable
fun StudentProfileScreen(viewModel: StudentProfileViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.onUiReady()
    }

    StudentProfileScreenContent(
        uiState,
        viewModel::onUiEvent,
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
    )

    HandleMessageState(
        message,
        snackbarHostState = snackbarHostState,
        onMessageHandled = viewModel::messageHandled,
    )
    HandleNavigationState(navigation, viewModel::navigationHandled)
}

@Composable
private fun HandleMessageState(
    message: StudentProfileMessageState?,
    snackbarHostState: SnackbarHostState,
    onMessageHandled: () -> Unit,
) {
    message?.let { state ->
        when (state) {
            is StudentProfileMessageState.CvDownloadSheet ->
                key(state.userId, state.url, state.openId) {
                    CvDownloadBottomSheet(
                        userId = state.userId,
                        userType = UserType.STUDENT,
                        url = state.url,
                        onDismiss = { isSuccess ->
                            onMessageHandled()
                            state.onDismiss(isSuccess)
                        },
                    )
                }
            is StudentProfileMessageState.CvUploadSheet ->
                key(state.userId, state.openId) {
                    CvUploadBottomSheet(
                        userId = state.userId,
                        userType = UserType.STUDENT,
                        onDismiss = { isSuccess ->
                            onMessageHandled()
                            state.onDismiss(isSuccess)
                        },
                    )
                }
            is StudentProfileMessageState.Loading -> {
                ShowLoaderDialog(cancelable = state.cancelable)
            }
            is StudentProfileMessageState.InputBio -> {
                ShowInputDialog(
                    title = stringResource(Res.string.txt_change_bio),
                    label = stringResource(Res.string.hint_bio),
                    input = state.currentBio,
                    singleLine = false,
                    minLines = 3,
                    maxLines = 5,
                    maxLength = 150,
                    confirmButtonText = stringResource(Res.string.txt_update),
                    onConfirm = { newBio ->
                        onMessageHandled()
                        state.onConfirm(newBio)
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is StudentProfileMessageState.CallConfirmation -> {
                ShowConfirmationDialog(
                    icon = Icons.Default.Call,
                    title = stringResource(Res.string.txt_title_call),
                    message = stringResource(Res.string.txt_msg_call).plus(" ${state.phoneNumber}"),
                    confirmButtonText = stringResource(Res.string.txt_call),
                    onConfirm = {
                        onMessageHandled()
                        state.onConfirm()
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is StudentProfileMessageState.EmailConfirmation -> {
                ShowConfirmationDialog(
                    icon = Icons.Default.Email,
                    title = stringResource(Res.string.txt_title_email),
                    message = stringResource(Res.string.txt_msg_email).plus(" ${state.email}"),
                    confirmButtonText = stringResource(Res.string.txt_email),
                    onConfirm = {
                        onMessageHandled()
                        state.onConfirm()
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is StudentProfileMessageState.ConfirmSignOut -> {
                ShowSignOutBottomSheet(
                    onConfirm = { signOutFromAllDevices ->
                        onMessageHandled()
                        state.onConfirm(signOutFromAllDevices)
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is StudentProfileMessageState.Success -> {
                ShowSuccessDialog(
                    message = state.message,
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled,
                )
            }
            is StudentProfileMessageState.Error -> {
                ShowErrorDialog(
                    message = state.message,
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled,
                )
            }
            is StudentProfileMessageState.Snackbar -> {
                HandleSnackbar(state.message, snackbarHostState, onMessageHandled)
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: StudentProfileNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(key1 = navigation) {
        navigation?.let {
            when (it) {
                is StudentProfileNavigationState.GoBack -> navigator?.goBack()
                is StudentProfileNavigationState.ResetToHome -> navigator?.resetTo(AppScreen.Home)
                is StudentProfileNavigationState.ImagePreviewScreen -> {
                    navigator?.navigateToImagePreview(it.encodedImageUrl)
                }
                is StudentProfileNavigationState.ImageUploadScreen -> {
                    navigator?.navigateTo(AppScreen.ImageUpload(it.userId, UserType.STUDENT))
                }
                is StudentProfileNavigationState.ChangePasswordScreen -> {
                    navigator?.navigateTo(AppScreen.ChangePassword())
                }
                is StudentProfileNavigationState.BloodDonationHistoryScreen -> {
                    navigator?.navigateTo(
                        AppScreen.BloodDonationHistory(it.userId, UserType.STUDENT),
                    )
                }
                is StudentProfileNavigationState.CheckInHistoryScreen -> {
                    navigator?.navigateToCheckInHistory(it.userId, UserType.STUDENT)
                }
                is StudentProfileNavigationState.StudentProfileEditScreen -> {
                    navigator?.navigateToStudentProfileEdit(it.userId)
                }
                is StudentProfileNavigationState.DeleteAccountScreen -> {
                    navigator?.navigateTo(AppScreen.DeleteAccount)
                }
            }
            onNavigationHandled()
        }
    }
}
