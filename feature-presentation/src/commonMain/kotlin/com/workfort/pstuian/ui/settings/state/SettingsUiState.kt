package com.workfort.pstuian.ui.settings.state

import androidx.compose.runtime.Immutable
import com.workfort.pstuian.featuredomain.model.AppUsageRole
import com.workfort.pstuian.featuredomain.model.ThemeMode

@Immutable
sealed interface SettingsUiState {

    data object None: SettingsUiState

    data class Content(
        val showNotification: Boolean,
        val theme: ThemeMode,
        val isDebug: Boolean,
        val fcmToken: String,
        val appUsageRole: AppUsageRole?,
        val appVersionName: String,
        val appVersionCode: Int,
        val deviceId: String,
    ): SettingsUiState
}
