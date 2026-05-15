package com.workfort.pstuian.ui.notification.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.notification.notification.composable.NotificationScreenContent
import com.workfort.pstuian.ui.notification.notification.state.NotificationNavigationState
import org.koin.compose.koinInject

@Composable
fun NotificationScreen(viewModel: NotificationViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    NotificationScreenContent(uiState, viewModel::onUiEvent)

    HandleNavigationState(navigation, viewModel::onNavigationConsumed)
}

@Composable
private fun HandleNavigationState(
    navigation: NotificationNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigation) {
        navigation?.let {
            when (it) {
                NotificationNavigationState.GoBack -> navigator?.goBack()
            }
            onNavigationHandled()
        }
    }
}
