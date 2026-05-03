package com.workfort.pstuian.ui.settings

import com.workfort.pstuian.featuredomain.model.DebugApiEnvironment
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.settings.state.AppPreferencePanelData
import com.workfort.pstuian.ui.settings.state.DebugPanelData
import com.workfort.pstuian.ui.settings.state.GeneralPanelData
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
        generalPanelData: GeneralPanelData,
        appPreferencePanelData: AppPreferencePanelData,
        debugPanelData: DebugPanelData?,
        appVersionName: String,
        appVersionCode: Int,
        deviceId: String,
    ) = updateUiState {
        SettingsUiState.Content(
            generalPanelData = generalPanelData,
            appPreferencePanelData = appPreferencePanelData,
            appVersionName = appVersionName,
            appVersionCode = appVersionCode,
            deviceId = deviceId,
            debugPanelData = debugPanelData,
        )
    }

    fun setTheme(theme: ThemeMode) = updateContent {
        copy(appPreferencePanelData = appPreferencePanelData.copy(theme = theme))
    }

    fun setFcmToken(fcmToken: String) = updateContent {
        copy(debugPanelData = debugPanelData?.copy(fcmToken = fcmToken))
    }

    fun setShowNotification(show: Boolean) = updateContent {
        copy(appPreferencePanelData = appPreferencePanelData.copy(showNotification = show))
    }

    fun setUserType(userType: UserType?) = updateContent {
        copy(generalPanelData = generalPanelData.copy(userType = userType))
    }

    fun setDebugApiEnvironment(environment: DebugApiEnvironment) = updateContent {
        copy(debugPanelData = debugPanelData?.copy(debugApiEnvironment = environment))
    }
}
