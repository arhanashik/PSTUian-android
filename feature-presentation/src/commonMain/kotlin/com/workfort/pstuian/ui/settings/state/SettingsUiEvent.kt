package com.workfort.pstuian.ui.settings.state

import com.workfort.pstuian.featuredomain.model.ThemeMode

sealed interface SettingsUiEvent {
    data object OnClickBack : SettingsUiEvent
    data object OnClickContactUs : SettingsUiEvent
    data class SetShowNotification(val show: Boolean) : SettingsUiEvent
    data class OnChangeTheme(val theme: ThemeMode) : SettingsUiEvent
    data object OnRefreshFcmToken : SettingsUiEvent
    data object OnClearSharedPrefs : SettingsUiEvent
    data object OnClickEditAppUsageRole : SettingsUiEvent
}
