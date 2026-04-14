package com.workfort.pstuian.ui.changepassword.state

import com.workfort.pstuian.featuredomain.model.ChangePasswordInput
import com.workfort.pstuian.featuredomain.model.ChangePasswordInputError

sealed interface ChangePasswordUiState {
    object None : ChangePasswordUiState

    data class Content(
        val input: ChangePasswordInput = ChangePasswordInput.INITIAL,
        val validationError: ChangePasswordInputError = ChangePasswordInputError.INITIAL,
        val isOperationLoading: Boolean = false,
    ) : ChangePasswordUiState
}
