package com.workfort.pstuian.ui.emailverification.state

import androidx.compose.runtime.Immutable
import com.workfort.pstuian.featuredomain.model.UserType

@Immutable
data class EmailVerificationUiState(
    val userType: UserType = UserType.STUDENT,
    val validationError: String? = null,
    val isLoading: Boolean = false,
    val messageState: MessageState? = null,
    val navigationState: NavigationState? = null,
) {
    sealed interface MessageState {
        data class EmailSentSuccess(val message: String) : MessageState
        data class Error(val message: String) : MessageState
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
    }
}
