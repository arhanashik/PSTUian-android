package com.workfort.pstuian.ui.changepassword

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.featuredomain.model.ChangePasswordInput
import com.workfort.pstuian.featuredomain.model.ChangePasswordInputError
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class ChangePasswordUiStateMachine : UiStateMachine<ChangePasswordUiState> {

    private val _state = MutableStateFlow<ChangePasswordUiState>(ChangePasswordUiState.None)
    override val uiState: StateFlow<ChangePasswordUiState> = _state.asStateFlow()

    private fun updateUiState(
        updater: ChangePasswordUiState.() -> ChangePasswordUiState,
    ) = _state.update(updater)

    fun showLoading(isLoading: Boolean) = updateUiState {
        when (this) {
            is ChangePasswordUiState.None -> this
            is ChangePasswordUiState.Content -> copy(isOperationLoading = isLoading)
        }
    }

    fun setInitialContent() = updateUiState {
        ChangePasswordUiState.Content()
    }

    fun updateInput(input: ChangePasswordInput, validationError: ChangePasswordInputError) = updateUiState {
        when (this) {
            is ChangePasswordUiState.None -> this
            is ChangePasswordUiState.Content -> copy(
                input = input,
                validationError = validationError,
            )
        }
    }
}
