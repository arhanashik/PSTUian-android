package com.workfort.pstuian.app.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.ui.common.domain.usecase.ClearAllDataUseCase
import com.workfort.pstuian.app.ui.common.domain.usecase.RegisterDeviceUseCase
import com.workfort.pstuian.app.ui.splash.SplashScreenState.DisplayState.MessageState
import com.workfort.pstuian.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authRepo: AuthRepository,
    private val clearAllDataUseCase: ClearAllDataUseCase,
    private val registerDeviceUseCase: RegisterDeviceUseCase,
    private val reducer: SplashScreenStateReducer,
) : ViewModel() {

    private val _splashScreenState = MutableStateFlow(reducer.initial)
    val splashScreenState: StateFlow<SplashScreenState> get() = _splashScreenState

    private fun updateScreenState(update: SplashScreenStateUpdate) =
        _splashScreenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(SplashScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(SplashScreenStateUpdate.NavigationConsumed)

    // Each time user opens the app,
    // we need to do following things in sequence
    // 1. registerDevice(api will automatically update if device already exists)
    // 3. getConfig and check the config
    fun checkAuth() {
        registerDevice()
    }

    fun registerDevice() {
        updateScreenState(SplashScreenStateUpdate.UpdateLoadingText("Checking device"))
        viewModelScope.launch {
            runCatching {
                registerDeviceUseCase()
            }.onSuccess {
                getConfig()
            }.onFailure {
                updateScreenState(
                    SplashScreenStateUpdate.UpdateMessageState(MessageState.DeviceRegFailed),
                )
            }
        }
    }

    fun getConfig() {
        updateScreenState(SplashScreenStateUpdate.UpdateLoadingText("Loading Configuration"))
        viewModelScope.launch {
            runCatching {
                authRepo.getConfig()
            }.onSuccess {
                if(it.forceUpdate != 0 && it.forceUpdateDone.not()) {
                    updateScreenState(
                        SplashScreenStateUpdate.UpdateMessageState(MessageState.ForceUpdate),
                    )
                    return@onSuccess
                }
                if(it.forceRefresh != 0 && it.forceRefreshDone.not()) {
                    updateScreenState(
                        SplashScreenStateUpdate.UpdateMessageState(MessageState.ForceRefresh),
                    )
                    return@onSuccess
                }
                updateScreenState(SplashScreenStateUpdate.GoToHomeScreen)
            }.onFailure {
                updateScreenState(
                    SplashScreenStateUpdate.UpdateMessageState(MessageState.GetConfigFailed),
                )
            }
        }
    }

    fun clearAllData() {
        updateScreenState(SplashScreenStateUpdate.UpdateLoadingText("Clearing data"))
        viewModelScope.launch {
            runCatching {
                clearAllDataUseCase()
            }.onSuccess {
                updateScreenState(SplashScreenStateUpdate.GoToHomeScreen)
            }.onFailure {
                updateScreenState(
                    SplashScreenStateUpdate.UpdateMessageState(MessageState.ForceRefresh),
                )
            }
        }
    }
}