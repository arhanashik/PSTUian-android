package com.workfort.pstuian.app.ui.commonmodel.splash

import com.workfort.pstuian.repository.AuthRepository
import com.workfort.pstuian.usecase.ClearAllDataUseCase
import com.workfort.pstuian.usecase.RegisterDeviceUseCase
import com.workfort.pstuian.reducer.ui.splash.SplashScreenState
import com.workfort.pstuian.reducer.ui.splash.SplashScreenStateReducer
import com.workfort.pstuian.reducer.ui.splash.SplashScreenStateUpdate
import com.workfort.pstuian.app.ui.commonmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authRepo: AuthRepository,
    private val clearAllDataUseCase: ClearAllDataUseCase,
    private val registerDeviceUseCase: RegisterDeviceUseCase,
    private val reducer: SplashScreenStateReducer,
) : BaseViewModel() {

    private val _splashScreenState = MutableStateFlow(reducer.initial)
    val splashScreenState: StateFlow<SplashScreenState> = _splashScreenState.asStateFlow()

    private fun updateScreenState(update: SplashScreenStateUpdate) =
        _splashScreenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(SplashScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(SplashScreenStateUpdate.NavigationConsumed)

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
                    SplashScreenStateUpdate.UpdateMessageState(
                        SplashScreenState.DisplayState.MessageState.DeviceRegFailed
                    ),
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
                        SplashScreenStateUpdate.UpdateMessageState(
                            SplashScreenState.DisplayState.MessageState.ForceUpdate
                        ),
                    )
                    return@onSuccess
                }
                if(it.forceRefresh != 0 && it.forceRefreshDone.not()) {
                    updateScreenState(
                        SplashScreenStateUpdate.UpdateMessageState(
                            SplashScreenState.DisplayState.MessageState.ForceRefresh
                        ),
                    )
                    return@onSuccess
                }
                updateScreenState(SplashScreenStateUpdate.GoToHomeScreen)
            }.onFailure {
                updateScreenState(
                    SplashScreenStateUpdate.UpdateMessageState(
                        SplashScreenState.DisplayState.MessageState.GetConfigFailed
                    ),
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
                    SplashScreenStateUpdate.UpdateMessageState(
                        SplashScreenState.DisplayState.MessageState.ForceRefresh
                    ),
                )
            }
        }
    }
}
