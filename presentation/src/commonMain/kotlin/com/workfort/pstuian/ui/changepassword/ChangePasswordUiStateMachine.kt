package com.workfort.pstuian.ui.changepassword

import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordInput
import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordInputError
import com.workfort.pstuian.ui.changepassword.screendata.ResetPasswordInput
import com.workfort.pstuian.ui.changepassword.screendata.ResetPasswordInputError
import com.workfort.pstuian.ui.changepassword.state.ChangePasswordUiState
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChangePasswordUiStateMachine : UiStateMachine<ChangePasswordUiState> {

    private val _state = MutableStateFlow<ChangePasswordUiState>(ChangePasswordUiState.None)
    override val uiState: StateFlow<ChangePasswordUiState> = _state.asStateFlow()

    // Cache panel inputs/errors so toggling panels preserves user-typed values.
    private var changePasswordInputCache: ChangePasswordInput = ChangePasswordInput.INITIAL
    private var changePasswordErrorCache: ChangePasswordInputError = ChangePasswordInputError.INITIAL
    private var sendResetEmailCache: String = ""
    private var sendResetEmailErrorCache: String = ""
    private var resetPasswordInputCache: ResetPasswordInput = ResetPasswordInput.INITIAL
    private var resetPasswordErrorCache: ResetPasswordInputError = ResetPasswordInputError.INITIAL

    private fun updateUiState(
        updater: ChangePasswordUiState.() -> ChangePasswordUiState,
    ) = _state.update(updater)

    fun transitToResetPasswordForm() = updateUiState {
        ChangePasswordUiState.ResetPassword(
            input = resetPasswordInputCache,
            validationError = resetPasswordErrorCache,
        )
    }

    fun transitToSendResetPasswordLink(prefilledEmail: String?) = updateUiState {
        val email = sendResetEmailCache.ifBlank { prefilledEmail.orEmpty() }
        sendResetEmailCache = email
        ChangePasswordUiState.SendResetPasswordLink(
            email = email,
            validationError = sendResetEmailErrorCache,
        )
    }

    fun transitToChangePasswordForm() = updateUiState {
        ChangePasswordUiState.ChangePassword(
            input = changePasswordInputCache,
            validationError = changePasswordErrorCache,
        )
    }

    fun updateChangePasswordInput(input: ChangePasswordInput) = updateUiState {
        changePasswordInputCache = input
        when (this) {
            is ChangePasswordUiState.ChangePassword -> copy(input = input)
            else -> this
        }
    }

    fun updateChangePasswordInputError(validationError: ChangePasswordInputError) = updateUiState {
        changePasswordErrorCache = validationError
        when (this) {
            is ChangePasswordUiState.ChangePassword -> copy(validationError = validationError)
            else -> this
        }
    }

    fun updateSendLinkEmail(email: String) = updateUiState {
        sendResetEmailCache = email
        when (this) {
            is ChangePasswordUiState.SendResetPasswordLink -> copy(email = email)
            else -> this
        }
    }

    fun setSendLinkValidationError(error: String) = updateUiState {
        sendResetEmailErrorCache = error
        when (this) {
            is ChangePasswordUiState.SendResetPasswordLink -> copy(validationError = error)
            else -> this
        }
    }

    fun updateResetPasswordInput(resetPasswordInput: ResetPasswordInput) = updateUiState {
        resetPasswordInputCache = resetPasswordInput
        when (this) {
            is ChangePasswordUiState.ResetPassword -> copy(input = resetPasswordInputCache)
            else -> this
        }
    }

    fun setResetPasswordInputError(validationError: ResetPasswordInputError) = updateUiState {
        resetPasswordErrorCache = validationError
        when (this) {
            is ChangePasswordUiState.ResetPassword ->
                copy(validationError = validationError)
            else -> this
        }
    }
}
