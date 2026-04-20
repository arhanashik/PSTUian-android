package com.workfort.pstuian.ui.settings

import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.settings.state.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsUiStateMachine : UiStateMachine<SettingsUiState> {
    private val _uiState = MutableStateFlow<SettingsUiState>(SettingsUiState.None)
    override val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private fun updateUiState(updater: SettingsUiState.() -> SettingsUiState) = _uiState.update(updater)

    private fun updateContent(
        updater: SettingsUiState.Content.() -> SettingsUiState.Content,
    ) = updateUiState {
        if (this is SettingsUiState.Content) {
            updater()
        } else {
            this
        }
    }

    fun showInitialState(
        showNotification: Boolean,
        theme: ThemeMode,
        isDebug: Boolean,
        fcmToken: String,
    ) = updateUiState {
        SettingsUiState.Content(
            showNotification = showNotification,
            theme = theme,
            isDebug = isDebug,
            fcmToken = fcmToken,
        )
    }

    fun setTheme(theme: ThemeMode) = updateContent {
        copy(theme = theme)
    }

    fun setFcmToken(fcmToken: String) = updateContent {
        copy(fcmToken = fcmToken)
    }

    fun setShowNotification(show: Boolean) = updateContent {
        copy(showNotification = show)
    }
}
