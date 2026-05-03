package com.workfort.pstuian.ui.settings.state

import com.workfort.pstuian.featuredomain.model.DebugApiEnvironment
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.model.UserType

sealed interface SettingsMessageState {
    data class Error(val message: String) : SettingsMessageState
    data class ConfirmAction(
        val title: String,
        val message: String,
        val onConfirm: () -> Unit,
    ) : SettingsMessageState
    data class UserTypeSelection(
        val selectedUserType: UserType?,
        val onSaveAndContinue: (UserType?) -> Unit,
    ) : SettingsMessageState
    data class ThemeSelection(
        val selectedTheme: ThemeMode,
        val onApply: (ThemeMode?) -> Unit,
    ) : SettingsMessageState
    data class DebugApiEnvironmentSelection(
        val selectedEnvironment: DebugApiEnvironment,
        val onApply: (DebugApiEnvironment?) -> Unit,
    ) : SettingsMessageState
}
