package com.workfort.pstuian.app.ui.settings


data class SettingsScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val showNotification: Boolean,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                showNotification = true,
                messageState = null,
            )
        }
        sealed interface MessageState {
            data class Success(val message: String) : MessageState
            data class Error(val message: String) : MessageState
        }
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
    }
}