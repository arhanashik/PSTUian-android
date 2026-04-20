package com.workfort.pstuian.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.workfort.pstuian.ui.common.composable.ShowErrorDialog
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import com.workfort.pstuian.ui.splash.composable.SplashContentPanel
import com.workfort.pstuian.ui.splash.state.SplashMessageState
import com.workfort.pstuian.ui.splash.state.SplashNavigationState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.message_force_refresh_dialog
import pstuian.feature_presentation.generated.resources.message_force_update_dialog
import pstuian.feature_presentation.generated.resources.title_force_refresh_dialog
import pstuian.feature_presentation.generated.resources.title_force_update_dialog

@Composable
fun SplashScreen(viewModel: SplashViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val navigation by viewModel.navigation.collectAsState()

    SplashContentPanel(uiState, viewModel::onUiEvent)

    HandleNavigationState(navigation, viewModel::onNavigationHandled)
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
                    navigator?.resetTo(AppScreen.Home)
                }
            }
            onNavigationHandled()
        }
    }
}
