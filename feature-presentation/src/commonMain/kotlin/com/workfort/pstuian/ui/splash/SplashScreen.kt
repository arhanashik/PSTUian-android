package com.workfort.pstuian.ui.splash

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.workfort.pstuian.ui.common.composable.UserTypeSelectionBottomSheet
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import com.workfort.pstuian.ui.splash.composable.SplashContentPanel
import com.workfort.pstuian.ui.splash.state.SplashMessageState
import com.workfort.pstuian.ui.splash.state.SplashNavigationState
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(viewModel: SplashViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    SplashContentPanel(uiState, viewModel::onUiEvent)

    HandleMessageState(message, viewModel::onMessageHandled)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@Composable
private fun HandleMessageState(
    message: SplashMessageState?,
    onMessageHandled: () -> Unit,
) {
    message?.let {
        when (it) {
            is SplashMessageState.UserTypeSelection -> {
                UserTypeSelectionBottomSheet(
                    selectedUserType = it.selectedUserType,
                    onSaveAndContinue = { userType -> it.onSaveAndContinue(userType) },
                )
            }
        }
    }
}


@Composable
private fun HandleNavigationState(
    navigationState: SplashNavigationState?,
    onNavigationHandled: () -> Unit,
) {
    val navigator = koinInject<AppNavigator?>()

    LaunchedEffect(navigationState) {
        navigationState?.let {
            when (it) {
                is SplashNavigationState.HomeScreen -> {
                    navigator?.resetAll(AppScreen.Home)
                }
            }
            onNavigationHandled()
        }
    }
}
