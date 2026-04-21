package com.workfort.pstuian.ui.splash

import com.workfort.pstuian.featuredomain.model.AppUsageRole
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
        actionBtnText: String?,
    ) {
        _uiState.update {
            val clearRolePicker = when {
                screenState == null -> true
                screenState is InitialScreenState.Home -> false
                else -> true
            }
            it.copy(
                screenState = screenState,
                statusText = statusText,
                descriptionText = descriptionText,
                actionBtnText = actionBtnText,
                showAppUsageRolePicker = if (clearRolePicker) false else it.showAppUsageRolePicker,
                selectedAppUsageRole = if (clearRolePicker) null else it.selectedAppUsageRole,
            )
        }
    }

    fun setShowAppUsageRolePicker(show: Boolean, initialSelection: AppUsageRole? = null) {
        _uiState.update {
            it.copy(
                showAppUsageRolePicker = show,
                selectedAppUsageRole = if (show) initialSelection else null,
            )
        }
    }

    fun setSelectedAppUsageRole(role: AppUsageRole?) {
        _uiState.update { it.copy(selectedAppUsageRole = role) }
    }
}
