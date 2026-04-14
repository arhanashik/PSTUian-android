package com.workfort.pstuian.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.workfort.pstuian.ui.splash.composable.SplashContentPanel
import com.workfort.pstuian.ui.splash.state.SplashNavigationState

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel,
    onNavigateToHome: () -> Unit,
    onUpdateApp: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onUiReady()
    }

    uiState.navigationState?.let { navigationState ->
        when (navigationState) {
            is SplashNavigationState.HomeScreen -> {
                onNavigateToHome()
                viewModel.navigationConsumed()
            }
        }
    }

    SplashContentPanel(
        modifier = modifier,
        state = uiState,
        onEvent = { event ->
            when (event) {
                is com.workfort.pstuian.ui.splash.state.SplashUiEvent.UpdateApp -> onUpdateApp()
                else -> viewModel.onEvent(event)
            }
        },
    )
}
