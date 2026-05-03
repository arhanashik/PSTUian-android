package com.workfort.pstuian.ui.settings.state

import androidx.compose.runtime.Immutable
import com.workfort.pstuian.featuredomain.model.DebugApiEnvironment
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.model.UserType

@Immutable
sealed interface SettingsUiState {

    data object None: SettingsUiState

    data class Content(
        val generalPanelData: GeneralPanelData,
        val appPreferencePanelData: AppPreferencePanelData,
        val debugPanelData: DebugPanelData?,
        val appVersionName: String,
        val appVersionCode: Int,
        val deviceId: String,
    ): SettingsUiState
}

data class GeneralPanelData(
    val userType: UserType?,
)

data class AppPreferencePanelData(
    val theme: ThemeMode,
    val showNotification: Boolean,
)

data class DebugPanelData(
    val fcmToken: String,
    val debugApiEnvironment: DebugApiEnvironment,
)
