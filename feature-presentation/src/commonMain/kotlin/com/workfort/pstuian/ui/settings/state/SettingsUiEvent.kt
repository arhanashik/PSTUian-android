package com.workfort.pstuian.ui.settings.state

import com.workfort.pstuian.featuredomain.model.DebugApiEnvironment
import com.workfort.pstuian.featuredomain.model.ThemeMode

sealed interface SettingsUiEvent {
    data object BackClicked : SettingsUiEvent
    data object UserTypeClicked : SettingsUiEvent
    data object ThemeClicked : SettingsUiEvent
    data class ShowNotificationToggled(val show: Boolean) : SettingsUiEvent
    data class ChangeThemeClicked(val theme: ThemeMode) : SettingsUiEvent
    data object RefreshFcmTokenClicked : SettingsUiEvent
    data object ClearCacheClicked : SettingsUiEvent
    data object ForceSignOutClicked : SettingsUiEvent
    data object DebugApiServerClicked : SettingsUiEvent
    data class DebugApiEnvironmentSelected(val environment: DebugApiEnvironment) : SettingsUiEvent
}
