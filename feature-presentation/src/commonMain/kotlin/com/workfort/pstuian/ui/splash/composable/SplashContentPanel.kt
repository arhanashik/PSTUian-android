package com.workfort.pstuian.ui.splash.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.usecase.InitialScreenState
import com.workfort.pstuian.ui.common.composable.AppScaffold
import com.workfort.pstuian.ui.common.composable.DraggableAdaptiveLoader
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.AppTheme
import com.workfort.pstuian.ui.common.theme.ApplySystemBarColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import com.workfort.pstuian.ui.splash.state.SplashUiEvent
import com.workfort.pstuian.ui.splash.state.SplashUiState
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.app_name

@Composable
fun SplashContentPanel(
    state: SplashUiState,
    onEvent: (SplashUiEvent) -> Unit,
) {
    ApplySystemBarColors(
        statusBarColor = MaterialTheme.colorScheme.background,
        statusBarDarkIcons = MaterialTheme.colorScheme.background.luminance() > 0.5f,
        navigationBarColor = MaterialTheme.colorScheme.background,
        navigationBarDarkIcons = MaterialTheme.colorScheme.background.luminance() > 0.5f,
    )

    AppScaffold {
        when {
            state.screenState == null || state.screenState is InitialScreenState.Home -> {
                DefaultSplashContent(state.statusText)
            }
            else -> {
                val isForceUpdateAction = state.screenState is InitialScreenState.ForceUpdate
                ErrorContent(
                    screenState = state.screenState,
                    statusText = state.statusText,
                    descriptionText = state.descriptionText,
                    showContinueAnyway = state.showContinueAnyway,
                    onClickAction = {
                        onEvent(SplashUiEvent.ActionBtnClicked(isForceUpdateAction))
                    },
                    onContinueAnywayClick = { onEvent(SplashUiEvent.ContinueAnywayClicked) },
                )
            }
        }
    }
}

@Composable
private fun DefaultSplashContent(loadingText: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        DraggableAdaptiveLoader()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
        ) {
            Text(
                text = stringResource(Res.string.app_name),
                style = TextStyle.title1.copy(color = AppColors.textPrimary),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = loadingText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
private fun SplashContentPanelLoadingPreview() {
    AppTheme {
        SplashContentPanel(
            state = SplashUiState(
                statusText = "Checking for updates..."
            ),
            onEvent = {}
        )
    }
}

@Preview
@Composable
private fun SplashContentPanelDeviceRegistrationFailedPreview() {
    AppTheme {
        SplashContentPanel(
            state = SplashUiState(
                screenState = InitialScreenState.MissingDeviceInfo,
                statusText = "Device Not Recognized",
                descriptionText = "Device is not recognized by server",
                showContinueAnyway = true,
            ),
            onEvent = {}
        )
    }
}

@Preview
@Composable
private fun SplashContentPanelErrorPreview() {
    AppTheme {
        SplashContentPanel(
            state = SplashUiState(
                screenState = InitialScreenState.MissingConfig,
                statusText = "Missing config",
                descriptionText = "Client and server out of sync",
                showContinueAnyway = true,
            ),
            onEvent = {}
        )
    }
}

@Preview
@Composable
private fun SplashContentPanelMaintenancePreview() {
    AppTheme {
        SplashContentPanel(
            state = SplashUiState(
                screenState = InitialScreenState.Maintenance(600000.0),
                statusText = "Maintenance Mode",
                descriptionText = "The server is currently under maintenance. Please check back later.",
            ),
            onEvent = {}
        )
    }
}
