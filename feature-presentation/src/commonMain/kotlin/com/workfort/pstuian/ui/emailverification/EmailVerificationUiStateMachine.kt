package com.workfort.pstuian.ui.emailverification

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.emailverification.state.EmailVerificationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EmailVerificationUiStateMachine : UiStateMachine<EmailVerificationUiState> {
    private val _uiState = MutableStateFlow(EmailVerificationUiState())
    override val uiState: StateFlow<EmailVerificationUiState> = _uiState.asStateFlow()

    fun onClickBack() {
        _uiState.update { it.copy(navigationState = EmailVerificationUiState.NavigationState.GoBack) }
    }

    fun onClickUserTypeBtn(userType: com.workfort.pstuian.featuredomain.model.UserType) {
        _uiState.update { it.copy(userType = userType) }
    }

    fun onClickSignIn() {
        _uiState.update { it.copy(navigationState = EmailVerificationUiState.NavigationState.GoBack) }
    }

    fun messageConsumed() {
        _uiState.update { it.copy(messageState = null) }
    }

    fun navigationConsumed() {
        _uiState.update { it.copy(navigationState = null) }
    }

    fun updateLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun updateValidationError(validationError: String?) {
        _uiState.update { it.copy(validationError = validationError) }
    }

    fun showSuccess(message: String) {
        _uiState.update {
            it.copy(
                isLoading = false,
                messageState = EmailVerificationUiState.MessageState.EmailSentSuccess(
                    message = message,
                ),
            )
        }
    }

    fun showError(message: String) {
        _uiState.update {
            it.copy(
                isLoading = false,
                messageState = EmailVerificationUiState.MessageState.Error(message),
            )
        }
    }
}
