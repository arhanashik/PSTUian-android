package com.workfort.pstuian.ui.settings.state

sealed interface SettingsUiEvent {
    data object BackClicked : SettingsUiEvent
    data object UserTypeClicked : SettingsUiEvent
    data object ThemeClicked : SettingsUiEvent
    data class ShowNotificationToggled(val show: Boolean) : SettingsUiEvent
    data object RefreshFcmTokenClicked : SettingsUiEvent
    data object ClearCacheClicked : SettingsUiEvent
    data object ForceSignOutClicked : SettingsUiEvent
    data object DebugApiServerClicked : SettingsUiEvent
}
