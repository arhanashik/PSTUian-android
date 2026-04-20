package com.workfort.pstuian.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.workfort.pstuian.ui.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.ShowErrorDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import com.workfort.pstuian.ui.home.composable.HomeScreenContent
import com.workfort.pstuian.ui.home.state.HomeMessageState
import com.workfort.pstuian.ui.home.state.HomeNavigationState
import com.workfort.pstuian.ui.home.state.HomeUiEvent
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.data_clear_message
import pstuian.feature_presentation.generated.resources.label_are_you_sure
import pstuian.feature_presentation.generated.resources.msg_request_notification_permission
import pstuian.feature_presentation.generated.resources.msg_sign_in_required
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
            is HomeMessageState.ClearAllData -> {
                ShowConfirmationDialog(
                    title = stringResource(Res.string.label_are_you_sure),
                    message = stringResource(Res.string.data_clear_message),
                    onConfirm = {
                        onUiEvent(HomeUiEvent.ClearDataClicked)
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
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: HomeNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                is HomeNavigationState.SplashScreen -> {
                    // TODO navigate to splash
                }
                is HomeNavigationState.SignInScreen -> {
                    navigator?.navigateTo(AppScreen.SignIn)
                }
                is HomeNavigationState.GoToProfileScreen -> {
                    navigator?.navigateTo(AppScreen.Profile(it.userId, it.userType))
                }
                is HomeNavigationState.NotificationScreen -> {
                    // TODO navigate to notification
                }
                is HomeNavigationState.FacultyScreen -> {
                     navigator?.navigateTo(AppScreen.Faculty(it.faculty.id))
                }
                is HomeNavigationState.ImagePreviewScreen -> {
                    // TODO navigate to image preview
                }
                is HomeNavigationState.ContactUsScreen -> {
                    // TODO navigate to contact us
                }
                is HomeNavigationState.DonorsScreen -> {
                    navigator?.navigateTo(AppScreen.BloodDonationRequestList)
                }
                is HomeNavigationState.BloodDonationRequestScreen -> {
                    navigator?.navigateTo(AppScreen.BloodDonationRequestCreate)
                }
                is HomeNavigationState.CheckInScreen -> {
                    // TODO navigate to check in
                }
                is HomeNavigationState.DonateScreen -> {
                    navigator?.navigateTo(AppScreen.Donate)
                }
                is HomeNavigationState.SettingsScreen -> {
                    navigator?.navigateTo(AppScreen.Settings)
                }
                is HomeNavigationState.Browser -> {
                    // TODO open browser
                }
                is HomeNavigationState.Store -> {
                    // TODO open store
                }
            }
            onNavigationHandled()
        }
    }
}
