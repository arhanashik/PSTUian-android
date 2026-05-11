package com.workfort.pstuian.ui.splash

import com.workfort.pstuian.featuredomain.usecase.InitialScreenState
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.splash.state.SplashUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SplashUiStateMachine : UiStateMachine<SplashUiState> {
    private val _uiState = MutableStateFlow(SplashUiState())
    override val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    fun updateScreenState(
        screenState: InitialScreenState?,
        statusText: String,
        descriptionText: String?,
        showContinueAnyway: Boolean = false,
    ) {
        _uiState.update {
            it.copy(
                screenState = screenState,
                statusText = statusText,
                descriptionText = descriptionText,
                showContinueAnyway = showContinueAnyway,
            )
        }
    }
}
