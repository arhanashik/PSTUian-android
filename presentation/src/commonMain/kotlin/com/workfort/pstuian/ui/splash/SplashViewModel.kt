package com.workfort.pstuian.ui.splash

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.repository.AppConfigRepository
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.featuredomain.usecase.GetInitialScreenUseCase
import com.workfort.pstuian.featuredomain.usecase.GetSignedInUserUseCase
import com.workfort.pstuian.featuredomain.usecase.InitialScreenState
import com.workfort.pstuian.featuredomain.usecase.RegisterDeviceUseCase
import com.workfort.pstuian.model.SharedScreenData
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.splash.state.SplashMessageState
import com.workfort.pstuian.ui.splash.state.SplashNavigationState
import com.workfort.pstuian.ui.splash.state.SplashUiEvent
import com.workfort.pstuian.ui.splash.state.SplashUiState
import com.workfort.pstuian.util.PlatformInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SplashViewModel(
    private val appConfigRepository: AppConfigRepository,
    private val registerDeviceUseCase: RegisterDeviceUseCase,
    private val getInitialScreenUseCase: GetInitialScreenUseCase,
    private val getSignedInUserUseCase: GetSignedInUserUseCase,
    private val authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository,
    private val sharedScreenData: SharedScreenData,
    private val platformInfo: PlatformInfo,
    private val stateMachine: SplashUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<SplashUiState>(stateMachine) {

    private val _message = MutableStateFlow<SplashMessageState?>(null)
    val message: StateFlow<SplashMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<SplashNavigationState?>(null)
    val navigation: StateFlow<SplashNavigationState?> = _navigation.asStateFlow()

    override fun onUiReady() {
        refreshConfig()
    }

    fun onUiEvent(event: SplashUiEvent) {
        when (event) {
            is SplashUiEvent.ActionBtnClicked -> {
                if (event.isForceUpdateAction) {
                    _navigation.update { SplashNavigationState.OpenUrl(platformInfo.storeUrl) }
                } else {
                    refreshConfig()
                }
            }
            SplashUiEvent.ContinueAnywayClicked -> {
                _navigation.update { SplashNavigationState.HomeScreen }
            }
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun refreshConfig() {
        applyScreenState(
            screenState = null,
            statusText = "Checking config...",
            descriptionText = null,
        )

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            val deviceResult = registerDeviceUseCase()
            val appConfig = appConfigRepository.getAppConfig()

            val initialScreen = getInitialScreenUseCase(
                device = deviceResult.getOrNull(),
                appConfig = appConfig,
            )
            handleInitialScreen(initialScreen, deviceResult.exceptionOrNull()?.message)
        }
    }

    private fun applyScreenState(
        screenState: InitialScreenState?,
        statusText: String,
        descriptionText: String?,
    ) {
        val showContinueAnyway =
            platformInfo.isDebug &&
                screenState != null &&
                screenState !is InitialScreenState.Home
        stateMachine.updateScreenState(
            screenState = screenState,
            statusText = statusText,
            descriptionText = descriptionText,
            showContinueAnyway = showContinueAnyway,
        )
    }

    private suspend fun handleInitialScreen(screenState: InitialScreenState, errorMessage: String? = null) {
        when (screenState) {
            is InitialScreenState.MissingDeviceInfo -> {
                applyScreenState(
                    screenState,
                    statusText = "Device Not Recognized",
                    descriptionText = errorMessage ?: "Device is not recognized by server",
                )
            }
            is InitialScreenState.DeviceBlocklisted -> {
                applyScreenState(
                    screenState,
                    statusText = "Access Denied",
                    descriptionText = "Please contact support",
                )
            }
            is InitialScreenState.MissingConfig -> {
                applyScreenState(
                    screenState,
                    statusText = "Missing config",
                    descriptionText = "Client and server out of sync",
                )
            }
            is InitialScreenState.ForceUpdate -> {
                applyScreenState(
                    screenState,
                    statusText = "Update Required",
                    descriptionText = "A new version is available to update!",
                )
            }
            is InitialScreenState.Maintenance -> {
                applyScreenState(
                    screenState,
                    statusText = "Under Maintenance",
                    descriptionText = null,
                )
            }
            is InitialScreenState.Home -> {
                // Syncing user info in sharedScreenData even though it's done from AppViewModel as well.
                // Because, sometimes, it might take some time to load the data.
                // In that case home screen is already shown with incorrect user data.
                val signInUser = getSignedInUserUseCase()
                sharedScreenData.setCurrentUser(signInUser)

                authRepository.syncAuthTokenToPreferences()
                applyScreenState(
                    screenState,
                    statusText = "All Done",
                    descriptionText = null,
                )
                checkUserTypeAndNavigateToHome()
            }
        }
    }

    private fun checkUserTypeAndNavigateToHome() {
        val selectedUserType = settingsRepository.getUserType()
        if (selectedUserType == null) {
            _message.update {
                SplashMessageState.UserTypeSelection(selectedUserType) { userType ->
                    settingsRepository.setUserType(userType)
                    _navigation.update { SplashNavigationState.HomeScreen }
                }
            }
        } else {
            _navigation.update { SplashNavigationState.HomeScreen }
        }
    }
}
