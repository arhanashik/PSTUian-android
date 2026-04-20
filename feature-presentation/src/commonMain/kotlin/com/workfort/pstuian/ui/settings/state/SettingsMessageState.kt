package com.workfort.pstuian.ui.settings.state

sealed interface SettingsMessageState {
    data class Error(val message: String) : SettingsMessageState
    data class ConfirmClearPrefs(
        val title: String,
        val message: String,
        val onConfirm: () -> Unit,
    ) : SettingsMessageState
}
