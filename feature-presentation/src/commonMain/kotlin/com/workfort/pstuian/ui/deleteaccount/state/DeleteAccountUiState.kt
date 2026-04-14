package com.workfort.pstuian.ui.deleteaccount.state

import androidx.compose.runtime.Immutable

@Immutable
data class DeleteAccountUiState(
    val input: String = "",
    val validationError: String = "",
    val messageState: MessageState? = null,
    val navigationState: NavigationState? = null,
) {
    sealed interface MessageState {
        data class Loading(val cancelable: Boolean) : MessageState
        data object ConfirmAccountDelete : MessageState
        data class Success(val message: String) : MessageState
        data class Error(val message: String) : MessageState
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
        data object ResetToContactUsScreen : NavigationState
        data object ResetToHomeScreen : NavigationState
    }
}
