package com.workfort.pstuian.ui.settings.state

import androidx.compose.runtime.Immutable

@Immutable
data class SettingsUiState(
    val showNotification: Boolean = true,
    val messageState: MessageState? = null,
    val navigationState: NavigationState? = null,
) {
    sealed interface MessageState {
        data class Error(val message: String) : MessageState
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
    }
}
