package com.workfort.pstuian.app.ui.deleteaccount


data class DeleteAccountScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val input: String,
        val validationError: String,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                input = "",
                validationError = "",
                messageState = null,
            )
        }
        sealed interface MessageState {
            data class Loading(val cancelable: Boolean) : MessageState
            data object ConfirmAccountDelete : MessageState
            data class Success(val message: String) : MessageState
            data class Error(val message: String) : MessageState
        }
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
        data object ResetToHomeScreen : NavigationState
        data object ResetToContactUsScreen : NavigationState
    }
}