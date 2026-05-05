package com.workfort.pstuian.ui.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import com.workfort.pstuian.ui.common.composable.ListSelectionBottomSheet
import com.workfort.pstuian.ui.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.userTypeListSelectionOptions
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.home.composable.HomeScreenContent
import com.workfort.pstuian.ui.home.state.HomeMessageState
import com.workfort.pstuian.ui.home.state.HomeNavigationState
import com.workfort.pstuian.ui.home.state.HomeUiEvent
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.btn_save_and_continue
import pstuian.feature_presentation.generated.resources.helper_app_usage_role_sheet
import pstuian.feature_presentation.generated.resources.msg_request_notification_permission
import pstuian.feature_presentation.generated.resources.msg_sign_in_required
import pstuian.feature_presentation.generated.resources.title_select_app_usage_role
import pstuian.feature_presentation.generated.resources.txt_allow
import pstuian.feature_presentation.generated.resources.txt_notification
import pstuian.feature_presentation.generated.resources.txt_sign_in
import pstuian.feature_presentation.generated.resources.txt_sign_in_required

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    HomeScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onUiEvent, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HandleMessageState(
    message: HomeMessageState?,
    onUiEvent: (HomeUiEvent) -> Unit,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is HomeMessageState.SignInNecessary -> {
                ShowConfirmationDialog(
                    title = stringResource(Res.string.txt_sign_in_required),
                    message = stringResource(Res.string.msg_sign_in_required),
                    confirmButtonText = stringResource(Res.string.txt_sign_in),
                    onConfirm = {
                        onMessageHandled()
                        onUiEvent(HomeUiEvent.SignInClicked)
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is HomeMessageState.NotificationPermission -> {
                ShowConfirmationDialog(
                    title = stringResource(Res.string.txt_notification),
                    message = stringResource(Res.string.msg_request_notification_permission),
                    confirmButtonText = stringResource(Res.string.txt_allow),
                    onConfirm = {
                        // TODO request permission
                        onMessageHandled()
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is HomeMessageState.ClearAllDataFailed -> {
                ShowErrorDialog(
                    message = it.error,
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled
                )
            }
            is HomeMessageState.UserTypeSelectionForSignIn -> {
                ListSelectionBottomSheet(
                    title = stringResource(Res.string.title_select_app_usage_role),
                    helperText = stringResource(Res.string.helper_app_usage_role_sheet),
                    primaryButtonLabel = stringResource(Res.string.btn_save_and_continue),
                    options = userTypeListSelectionOptions(),
                    initialSelection = it.selectedUserType,
                    scrollable = false,
                    onDismiss = onMessageHandled,
                    onConfirm = { userType -> it.onSaveAndContinue(userType) },
                )
            }
            is HomeMessageState.SignInNotSupportedForUserType -> {
                ShowErrorDialog(
                    message = it.message,
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled,
                )
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: HomeNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                is HomeNavigationState.SignInScreen -> {
                    navigator?.navigateToSignIn()
                }
                is HomeNavigationState.GoToProfileScreen -> {
                    navigator?.navigateToProfile(it.userId, it.userType)
                }
                is HomeNavigationState.NotificationScreen -> {
                    navigator?.navigateToNotification()
                }
                is HomeNavigationState.FacultyScreen -> {
                     navigator?.navigateToFaculty(it.faculty.id)
                }
                is HomeNavigationState.ImagePreviewScreen -> {
                    navigator?.navigateToImagePreview(it.url)
                }
                is HomeNavigationState.DonorsScreen -> {
                    navigator?.navigateToBloodDonationRequestList()
                }
                is HomeNavigationState.BloodDonationScreen -> {
                    navigator?.navigateToBloodDonationRequestList()
                }
                is HomeNavigationState.CheckInScreen -> {
                    navigator?.navigateToCheckIn()
                }
                is HomeNavigationState.SettingsScreen -> {
                    navigator?.navigateToSettings()
                }
                is HomeNavigationState.OpenUrl -> {
                    uriHandler.openUri(it.url)
                }
            }
            onNavigationHandled()
        }
    }
}
