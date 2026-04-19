package com.workfort.pstuian.ui.splash

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.repository.AppConfigRepository
import com.workfort.pstuian.featuredomain.usecase.ClearAllDataUseCase
import com.workfort.pstuian.featuredomain.usecase.GetInitialScreenUseCase
import com.workfort.pstuian.featuredomain.usecase.InitialScreenState
import com.workfort.pstuian.featuredomain.usecase.RegisterDeviceUseCase
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.splash.state.SplashMessageState
import com.workfort.pstuian.ui.splash.state.SplashNavigationState
import com.workfort.pstuian.ui.splash.state.SplashUiEvent
import com.workfort.pstuian.ui.splash.state.SplashUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SplashViewModel(
    private val appConfigRepository: AppConfigRepository,
    private val clearAllDataUseCase: ClearAllDataUseCase,
    private val registerDeviceUseCase: RegisterDeviceUseCase,
    private val getInitialScreenUseCase: GetInitialScreenUseCase,
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
        // Add event handling here
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun refreshConfig() {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            stateMachine.updateLoadingText("Checking config...")

            val initialScreen = getInitialScreenUseCase(
                device = registerDeviceUseCase().getOrNull(),
                appConfig = appConfigRepository.getAppConfig(),
            )
            stateMachine.updateLoadingText(initialScreen.name)
            when (initialScreen) {
                InitialScreenState.MISSING_CONFIG -> {
                    _message.update { SplashMessageState.GetConfigFailed(::refreshConfig) }
                }
                InitialScreenState.BLOCKLISTED -> {
                    _message.update { SplashMessageState.DeviceRegFailed(::refreshConfig) }
                }
                InitialScreenState.FORCE_UPDATE -> {
                    _message.update { SplashMessageState.ForceUpdate(::refreshConfig) }
                }
                InitialScreenState.MAINTENANCE -> {
                    _message.update { SplashMessageState.ForceRefresh(::refreshConfig) }
                }
                InitialScreenState.HOME -> _navigation.update { SplashNavigationState.HomeScreen }
            }
        }
    }

    private fun clearAllData() {
        stateMachine.updateLoadingText("Clearing data")
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                clearAllDataUseCase()
            }.onSuccess {
                _navigation.update { SplashNavigationState.HomeScreen }
            }.onFailure {
                _message.update {
                    SplashMessageState.ForceRefresh {
                        clearAllData()
                    }
                }
            }
        }
    }
}
