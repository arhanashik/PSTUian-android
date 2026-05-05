package com.workfort.pstuian.ui.splash

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import coil3.compose.LocalPlatformContext
import com.workfort.pstuian.ui.common.composable.clearSingletonCoilImageCaches
import com.workfort.pstuian.ui.common.composable.ListSelectionBottomSheet
import com.workfort.pstuian.ui.common.composable.userTypeListSelectionOptions
import com.workfort.pstuian.model.AppLaunchDeepLinkController
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.AppScreen
import com.workfort.pstuian.ui.common.navigation.DeepLinkNavigator
import com.workfort.pstuian.ui.splash.composable.SplashContentPanel
import com.workfort.pstuian.ui.splash.state.SplashMessageState
import com.workfort.pstuian.ui.splash.state.SplashNavigationState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.btn_save_and_continue
import pstuian.feature_presentation.generated.resources.helper_app_usage_role_sheet
import pstuian.feature_presentation.generated.resources.title_select_app_usage_role

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(viewModel: SplashViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val message by viewModel.message.collectAsState()
    val navigation by viewModel.navigation.collectAsState()
    val platformContext = LocalPlatformContext.current

    LaunchedEffect(Unit) {
        clearSingletonCoilImageCaches(platformContext)
    }

    SplashContentPanel(uiState, viewModel::onUiEvent)

    HandleMessageState(message = message)
    HandleNavigationState(navigation, viewModel::onNavigationHandled)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HandleMessageState(
    message: SplashMessageState?,
) {
    message?.let {
        when (it) {
            is SplashMessageState.UserTypeSelection -> {
                ListSelectionBottomSheet(
                    title = stringResource(Res.string.title_select_app_usage_role),
                    helperText = stringResource(Res.string.helper_app_usage_role_sheet),
                    primaryButtonLabel = stringResource(Res.string.btn_save_and_continue),
                    options = userTypeListSelectionOptions(),
                    initialSelection = it.selectedUserType,
                    scrollable = false,
                    onConfirm = { userType -> it.onSaveAndContinue(userType) },
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
    val deepLinkNavigator = koinInject<DeepLinkNavigator>()
    val launchDeepLinkController = koinInject<AppLaunchDeepLinkController>()

    LaunchedEffect(navigationState) {
        navigationState?.let {
            when (it) {
                is SplashNavigationState.HomeScreen -> {
                    navigator?.replaceAll(AppScreen.Home)
                    launchDeepLinkController.consumePendingDeepLink()?.let { action ->
                        deepLinkNavigator.navigate(action, navigator)
                    }
                }
            }
            onNavigationHandled()
        }
    }
}
