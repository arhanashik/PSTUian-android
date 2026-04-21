package com.workfort.pstuian.ui.splash

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.AppUsageRole
import com.workfort.pstuian.featuredomain.repository.AppConfigRepository
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.featuredomain.usecase.GetInitialScreenUseCase
import com.workfort.pstuian.featuredomain.usecase.InitialScreenState
import com.workfort.pstuian.featuredomain.usecase.RegisterDeviceUseCase
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.splash.state.SplashNavigationState
import com.workfort.pstuian.ui.splash.state.SplashUiEvent
import com.workfort.pstuian.ui.splash.state.SplashUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SplashViewModel(
    private val appConfigRepository: AppConfigRepository,
    private val registerDeviceUseCase: RegisterDeviceUseCase,
    private val getInitialScreenUseCase: GetInitialScreenUseCase,
    private val settingsRepository: SettingsRepository,
    private val stateMachine: SplashUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<SplashUiState>(stateMachine) {

    private val _navigation = MutableStateFlow<SplashNavigationState?>(null)
    val navigation: StateFlow<SplashNavigationState?> = _navigation.asStateFlow()

    override fun onUiReady() {
        refreshConfig()
    }

    fun onUiEvent(event: SplashUiEvent) {
        when (event) {
            is SplashUiEvent.ActionBtnClicked -> {
                if (event.isForceUpdateAction) {
                    // Navigate to app/play store
                } else {
                    refreshConfig()
                }
            }
            is SplashUiEvent.SelectAppUsageRole -> stateMachine.setSelectedAppUsageRole(event.role)
            SplashUiEvent.SaveAppUsageRoleAndContinue -> onSaveAppUsageRoleAndContinue()
        }
    }

    private fun onSaveAppUsageRoleAndContinue() {
        val selected = stateMachine.uiState.value.selectedAppUsageRole ?: return
        settingsRepository.setAppUsageRole(selected)
        stateMachine.setShowAppUsageRolePicker(false)
        _navigation.update { SplashNavigationState.HomeScreen }
    }

    fun onNavigationHandled() = _navigation.update { null }

    private fun refreshConfig() {
        stateMachine.updateScreenState(
            screenState = null,
            statusText = "Checking config...",
            descriptionText = null,
            actionBtnText = null,
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

    private fun handleInitialScreen(screenState: InitialScreenState, errorMessage: String? = null) {
        when (screenState) {
            is InitialScreenState.MissingDeviceInfo -> {
                stateMachine.updateScreenState(
                    screenState,
                    statusText = "Device Not Recognized",
                    descriptionText = errorMessage ?: "Device is not recognized by server",
                    actionBtnText = "Retry",
                )
            }
            is InitialScreenState.DeviceBlocklisted -> {
                stateMachine.updateScreenState(
                    screenState,
                    statusText = "Access Denied",
                    descriptionText = "Please contact support",
                    actionBtnText = null,
                )
            }
            is InitialScreenState.MissingConfig -> {
                stateMachine.updateScreenState(
                    screenState,
                    statusText = "Missing config",
                    descriptionText = "Client and server out of sync",
                    actionBtnText = "Refresh",
                )
            }
            is InitialScreenState.ForceUpdate -> {
                stateMachine.updateScreenState(
                    screenState,
                    statusText = "Update Required",
                    descriptionText = "A new version is available to update!",
                    actionBtnText = "Update",
                )
            }
            is InitialScreenState.Maintenance -> {
                stateMachine.updateScreenState(
                    screenState,
                    statusText = "Under Maintenance",
                    descriptionText = null,
                    actionBtnText = "Refresh",
                )
            }
            is InitialScreenState.Home -> {
                stateMachine.updateScreenState(
                    screenState,
                    statusText = "All Done",
                    descriptionText = null,
                    actionBtnText = null,
                )
                val stored = settingsRepository.getAppUsageRole()
                if (stored == null || stored == AppUsageRole.VISITOR) {
                    val initialSelection = stored?.takeIf { it == AppUsageRole.VISITOR }
                    stateMachine.setShowAppUsageRolePicker(show = true, initialSelection = initialSelection)
                } else {
                    _navigation.update { SplashNavigationState.HomeScreen }
                }
            }
        }
    }
}
