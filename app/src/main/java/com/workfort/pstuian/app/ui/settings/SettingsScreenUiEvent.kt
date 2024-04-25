package com.workfort.pstuian.app.ui.settings

sealed class SettingsScreenUiEvent {
    data object None : SettingsScreenUiEvent()
    data object LoadInitialData : SettingsScreenUiEvent()
    data object OnClickBack : SettingsScreenUiEvent()
    data object OnClickContactUs : SettingsScreenUiEvent()
    data class OnChangeShowNotification(val show: Boolean) : SettingsScreenUiEvent()
    data object MessageConsumed : SettingsScreenUiEvent()
    data object NavigationConsumed : SettingsScreenUiEvent()
}