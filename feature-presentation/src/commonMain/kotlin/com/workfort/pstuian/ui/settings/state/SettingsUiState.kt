package com.workfort.pstuian.ui.settings.state

import androidx.compose.runtime.Immutable
import com.workfort.pstuian.featuredomain.model.DebugApiEnvironment
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.model.UserType

@Immutable
sealed interface SettingsUiState {

    data object None: SettingsUiState

    data class Content(
        val userType: UserType?,
        val theme: ThemeMode,
        val showNotification: Boolean,
        val appVersionName: String,
        val appVersionCode: Int,
        val deviceId: String,
        val debugPanelData: DebugPanelData?,
    ): SettingsUiState
}

data class DebugPanelData(
    val fcmToken: String,
    val debugApiEnvironment: DebugApiEnvironment,
)
