package com.workfort.pstuian.ui.forgotpassword

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.forgotpassword.state.ForgotPasswordUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ForgotPasswordUiStateMachine : UiStateMachine<ForgotPasswordUiState> {
    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    override val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun showLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun changeUserType(userType: UserType) {
        _uiState.update { it.copy(userType = userType) }
    }

    fun setValidationError(error: String?) {
        _uiState.update { it.copy(validationError = error) }
    }
}
