package com.workfort.pstuian.ui.splash

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.usecase.ClearAllDataUseCase
import com.workfort.pstuian.featuredomain.usecase.RegisterDeviceUseCase
import com.workfort.pstuian.ui.splash.state.SplashMessageState
import com.workfort.pstuian.ui.splash.state.SplashNavigationState
import com.workfort.pstuian.ui.splash.state.SplashUiEvent
import com.workfort.pstuian.ui.splash.state.SplashUiState
import kotlinx.coroutines.launch

internal class SplashViewModel(
    private val authRepo: AuthRepository,
    private val clearAllDataUseCase: ClearAllDataUseCase,
    private val registerDeviceUseCase: RegisterDeviceUseCase,
    private val stateMachine: SplashUiStateMachine,
) : UiStateMachineViewModel<SplashUiState>(stateMachine) {

    override fun onUiReady() {
        checkAuth()
    }

    fun onEvent(event: SplashUiEvent) {
        when (event) {
            SplashUiEvent.CheckAuth -> checkAuth()
            SplashUiEvent.TryDeviceReg -> registerDevice()
            SplashUiEvent.TryGetConfig -> getConfig()
            SplashUiEvent.UpdateApp -> Unit // Handled in Screen
            SplashUiEvent.RefreshData -> clearAllData()
            SplashUiEvent.MessageConsumed -> messageConsumed()
            SplashUiEvent.NavigationConsumed -> navigationConsumed()
        }
    }

    private fun checkAuth() {
        registerDevice()
    }

    private fun registerDevice() {
        stateMachine.updateLoadingText("Checking device")
        viewModelScope.launch {
            runCatching {
                registerDeviceUseCase()
            }.onSuccess {
                getConfig()
            }.onFailure {
                stateMachine.showMessage(SplashMessageState.DeviceRegFailed)
            }
        }
    }

    private fun getConfig() {
        stateMachine.updateLoadingText("Loading Configuration")
        viewModelScope.launch {
            runCatching {
                authRepo.getConfig()
            }.onSuccess {
                if(it.forceUpdate != 0 && it.forceUpdateDone.not()) {
                    stateMachine.showMessage(SplashMessageState.ForceUpdate)
                    return@onSuccess
                }
                if(it.forceRefresh != 0 && it.forceRefreshDone.not()) {
                    stateMachine.showMessage(SplashMessageState.ForceRefresh)
                    return@onSuccess
                }
                stateMachine.navigateTo(SplashNavigationState.HomeScreen)
            }.onFailure {
                stateMachine.showMessage(SplashMessageState.GetConfigFailed)
            }
        }
    }

    private fun clearAllData() {
        stateMachine.updateLoadingText("Clearing data")
        viewModelScope.launch {
            runCatching {
                clearAllDataUseCase()
            }.onSuccess {
                stateMachine.navigateTo(SplashNavigationState.HomeScreen)
            }.onFailure {
                stateMachine.showMessage(SplashMessageState.ForceRefresh)
            }
        }
    }

    override fun messageConsumed() {
        stateMachine.showMessage(null)
    }

    override fun navigationConsumed() {
        stateMachine.navigateTo(null)
    }
}
