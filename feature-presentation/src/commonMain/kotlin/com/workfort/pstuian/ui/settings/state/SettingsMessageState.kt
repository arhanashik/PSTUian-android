package com.workfort.pstuian.ui.settings.state

import com.workfort.pstuian.featuredomain.model.UserType

sealed interface SettingsMessageState {
    data class Error(val message: String) : SettingsMessageState
    data class ConfirmClearPrefs(
        val title: String,
        val message: String,
        val onConfirm: () -> Unit,
    ) : SettingsMessageState
    data class UserTypeSelection(
        val selectedUserType: UserType?,
        val onSaveAndContinue: (UserType?) -> Unit,
    ) : SettingsMessageState
}
