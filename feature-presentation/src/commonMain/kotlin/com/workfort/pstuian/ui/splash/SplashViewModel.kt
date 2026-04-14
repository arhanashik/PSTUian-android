package com.workfort.pstuian.ui.splash

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.usecase.ClearAllDataUseCase
import com.workfort.pstuian.featuredomain.usecase.RegisterDeviceUseCase
import com.workfort.pstuian.ui.splash.state.SplashMessageState
import com.workfort.pstuian.ui.splash.state.SplashNavigationState
import com.workfort.pstuian.ui.splash.state.SplashUiEvent
import com.workfort.pstuian.ui.splash.state.SplashUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class SplashViewModel(
    private val authRepo: AuthRepository,
    private val clearAllDataUseCase: ClearAllDataUseCase,
    private val registerDeviceUseCase: RegisterDeviceUseCase,
    private val stateMachine: SplashUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<SplashUiState>(stateMachine) {

    private val _message = MutableStateFlow<SplashMessageState?>(null)
    val message: StateFlow<SplashMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<SplashNavigationState?>(null)
    val navigation: StateFlow<SplashNavigationState?> = _navigation.asStateFlow()

    override fun onUiReady() {
        checkAuth()
    }

    fun onUiEvent(event: SplashUiEvent) {
        // Add event handling here
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun checkAuth() {
        registerDevice()
    }

    private fun registerDevice() {
        stateMachine.updateLoadingText("Checking device")
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                registerDeviceUseCase()
            }.onSuccess {
                getConfig()
            }.onFailure {
                _message.update {
                    SplashMessageState.DeviceRegFailed {
                        registerDevice()
                    }
                }
            }
        }
    }

    private fun getConfig() {
        stateMachine.updateLoadingText("Loading Configuration")
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                authRepo.getConfig()
            }.onSuccess {
                if(it.forceUpdate != 0 && it.forceUpdateDone.not()) {
                    _message.update {
                        SplashMessageState.ForceUpdate {
                            // go to app/play store
                        }
                    }
                    return@onSuccess
                }
                if(it.forceRefresh != 0 && it.forceRefreshDone.not()) {
                    _message.update {
                        SplashMessageState.ForceRefresh {
                            clearAllData()
                        }
                    }
                    return@onSuccess
                }
                _navigation.update { SplashNavigationState.HomeScreen }
            }.onFailure {
                _message.update {
                    SplashMessageState.GetConfigFailed {
                        getConfig()
                    }
                }
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
