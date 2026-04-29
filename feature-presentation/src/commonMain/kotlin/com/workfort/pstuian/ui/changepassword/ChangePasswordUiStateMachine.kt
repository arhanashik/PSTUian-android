package com.workfort.pstuian.ui.changepassword

import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordInput
import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordInputError
import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordScreenPanel
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordUiState
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChangePasswordUiStateMachine : UiStateMachine<ChangePasswordUiState> {

    private val _state = MutableStateFlow<ChangePasswordUiState>(ChangePasswordUiState.None)
    override val uiState: StateFlow<ChangePasswordUiState> = _state.asStateFlow()

    private var changePasswordInputCache: ChangePasswordInput = ChangePasswordInput.INITIAL
    private var changePasswordInputErrorCache: ChangePasswordInputError = ChangePasswordInputError.INITIAL
    private var draftResetEmail: String = ""
    private var emailValidationErrorCache: String = ""

    private fun updateUiState(
        updater: ChangePasswordUiState.() -> ChangePasswordUiState,
    ) = _state.update(updater)

    fun setInitialContent() = updateUiState {
        ChangePasswordUiState.ChangePassword()
    }

    fun updateChangePasswordInput(input: ChangePasswordInput) = updateUiState {
        when (this) {
            is ChangePasswordUiState.ChangePassword -> copy(input = input)
            else -> this
        }
    }

    fun updateChangePasswordInputError(validationError: ChangePasswordInputError) = updateUiState {
        when (this) {
            is ChangePasswordUiState.ChangePassword -> copy(validationError = validationError)
            else -> this
        }
    }

    fun transitionTo(panel: ChangePasswordScreenPanel) = updateUiState {
        when (panel) {
            ChangePasswordScreenPanel.ChangePassword -> when (this) {
                is ChangePasswordUiState.None -> this
                is ChangePasswordUiState.ChangePassword -> this
                is ChangePasswordUiState.ResetPassword -> {
                    draftResetEmail = email
                    emailValidationErrorCache = validationError
                    ChangePasswordUiState.ChangePassword(changePasswordInputCache, changePasswordInputErrorCache)
                }
            }
            ChangePasswordScreenPanel.ResetPassword -> when (this) {
                is ChangePasswordUiState.None -> this
                is ChangePasswordUiState.ResetPassword -> this
                is ChangePasswordUiState.ChangePassword -> {
                    changePasswordInputCache = input
                    changePasswordInputErrorCache = validationError
                    ChangePasswordUiState.ResetPassword(
                        email = draftResetEmail,
                        validationError = emailValidationErrorCache,
                    )
                }
            }
        }
    }

    fun updateResetEmail(email: String) = updateUiState {
        when (this) {
            is ChangePasswordUiState.ResetPassword -> copy(email = email)
            else -> this
        }
    }

    fun updateResetEmailValidationError(validationError: String) = updateUiState {
        when (this) {
            is ChangePasswordUiState.ResetPassword -> copy(validationError = validationError)
            else -> this
        }
    }
}
