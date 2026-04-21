package com.workfort.pstuian.ui.settings.state

import com.workfort.pstuian.featuredomain.model.AppUsageRole

sealed interface SettingsMessageState {
    data class Error(val message: String) : SettingsMessageState
    data class ConfirmClearPrefs(
        val title: String,
        val message: String,
        val onConfirm: () -> Unit,
    ) : SettingsMessageState
    data class AppUsageRoleSelection(
        val selectedRole: AppUsageRole?,
        val onSelectRole: (AppUsageRole) -> Unit,
        val onSaveAndContinue: () -> Unit,
    ) : SettingsMessageState
}
