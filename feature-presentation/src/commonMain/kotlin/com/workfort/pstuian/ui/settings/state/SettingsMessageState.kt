package com.workfort.pstuian.ui.settings.state

sealed interface SettingsMessageState {
    data class Error(val message: String) : SettingsMessageState
}
