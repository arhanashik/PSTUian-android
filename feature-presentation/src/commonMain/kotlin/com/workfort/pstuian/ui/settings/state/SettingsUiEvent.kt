package com.workfort.pstuian.ui.settings.state

sealed interface SettingsUiEvent {
    data object OnClickBack : SettingsUiEvent
    data class SetShowNotification(val show: Boolean) : SettingsUiEvent
    data object MessageConsumed : SettingsUiEvent
    data object NavigationConsumed : SettingsUiEvent
}
