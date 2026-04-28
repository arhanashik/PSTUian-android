package com.workfort.pstuian.ui.checkinlist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import com.workfort.pstuian.ui.checkinlist.composable.CheckInListScreenContent
import com.workfort.pstuian.ui.checkinlist.state.CheckInListMessageState
import com.workfort.pstuian.ui.checkinlist.state.CheckInListNavigationState
import com.workfort.pstuian.ui.common.composable.ShowConfirmationDialog
import com.workfort.pstuian.ui.common.composable.ShowErrorDialog
import com.workfort.pstuian.ui.common.composable.ShowInfoDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.msg_confirm_check_in
import pstuian.feature_presentation.generated.resources.txt_call
import pstuian.feature_presentation.generated.resources.txt_check_in
import pstuian.feature_presentation.generated.resources.txt_msg_call
import pstuian.feature_presentation.generated.resources.txt_title_call

@Composable
fun CheckInListScreen(viewModel: CheckInListViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    CheckInListScreenContent(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled, viewModel::checkIn)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@Composable
private fun HandleMessageState(
    message: CheckInListMessageState?,
    onMessageHandled: () -> Unit,
    onCheckIn: (Int) -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    message?.let {
        when (it) {
            is CheckInListMessageState.Error -> {
                ShowErrorDialog(
                    message = it.message,
                    onConfirm = onMessageHandled,
                    onDismiss = onMessageHandled,
                )
            }
            is CheckInListMessageState.Success -> {
                ShowInfoDialog(
                    message = it.message,
                    onDismiss = onMessageHandled,
                )
            }
            is CheckInListMessageState.Call -> {
                ShowConfirmationDialog(
                    icon = Icons.Default.Notifications,
                    title = stringResource(Res.string.txt_title_call),
                    message = stringResource(Res.string.txt_msg_call).plus(" ${it.phoneNumber}"),
                    confirmButtonText = stringResource(Res.string.txt_call),
                    onConfirm = {
                        onMessageHandled()
                        uriHandler.openUri("tel:${it.phoneNumber}")
                    },
                    onDismiss = onMessageHandled,
                )
            }
            is CheckInListMessageState.ConfirmCheckIn -> {
                ShowConfirmationDialog(
                    icon = Icons.Default.Notifications,
                    message = stringResource(Res.string.msg_confirm_check_in)
                        .plus(" ${it.location.name}"),
                    confirmButtonText = stringResource(Res.string.txt_check_in),
                    onConfirm = {
                        onCheckIn(it.location.id)
                        onMessageHandled()
                    },
                    onDismiss = onMessageHandled,
                )
            }
        }
    }
}

@Composable
private fun HandleNavigationState(
    navigation: CheckInListNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                is CheckInListNavigationState.GoBack -> navigator?.goBack()
                is CheckInListNavigationState.ProfileScreen -> {
                    navigator?.navigateTo(AppScreen.Profile(it.userId, it.userType))
                }
                is CheckInListNavigationState.LocationPickerScreen -> {
                    navigator?.navigateTo(AppScreen.LocationPicker)
                }
            }
            onNavigationHandled()
        }
    }
}
