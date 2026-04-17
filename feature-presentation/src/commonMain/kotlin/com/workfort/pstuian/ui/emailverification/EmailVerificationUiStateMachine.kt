package com.workfort.pstuian.ui.emailverification

import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.emailverification.state.EmailVerificationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EmailVerificationUiStateMachine : UiStateMachine<EmailVerificationUiState> {
    private val _uiState = MutableStateFlow(EmailVerificationUiState())
    override val uiState: StateFlow<EmailVerificationUiState> = _uiState.asStateFlow()

    fun onClickUserTypeBtn(userType: UserType) {
        _uiState.update { it.copy(userType = userType) }
    }

    fun updateLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun updateValidationError(validationError: String?) {
        _uiState.update { it.copy(validationError = validationError) }
    }
}
