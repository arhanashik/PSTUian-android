package com.workfort.pstuian.ui.splash

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.splash.state.SplashMessageState
import com.workfort.pstuian.ui.splash.state.SplashNavigationState
import com.workfort.pstuian.ui.splash.state.SplashUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SplashUiStateMachine : UiStateMachine<SplashUiState> {
    private val _uiState = MutableStateFlow(SplashUiState())
    override val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    fun updateLoadingText(text: String) {
        _uiState.update { it.copy(loadingText = text) }
    }

    fun showMessage(messageState: SplashMessageState?) {
        _uiState.update { it.copy(messageState = messageState) }
    }

    fun navigateTo(navigationState: SplashNavigationState?) {
        _uiState.update { it.copy(navigationState = navigationState) }
    }
}
