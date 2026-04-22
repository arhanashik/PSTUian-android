package com.workfort.pstuian.ui.signin.state

import com.workfort.pstuian.ui.signin.screendata.SignInFormData
import com.workfort.pstuian.ui.signin.screendata.SignUpFormData

sealed interface SignInUiState {
    val isLoading: Boolean

    data class None(override val isLoading: Boolean = false) : SignInUiState

    data class SignInPanel(
        override val isLoading: Boolean,
        val formData: SignInFormData,
        val rememberMe: Boolean,
    ) : SignInUiState

    data class SignUpPanel(
        override val isLoading: Boolean,
        val formData: SignUpFormData,
    ) : SignInUiState

    data class ForgotPasswordPanel(
        override val isLoading: Boolean,
        val email: String,
    ) : SignInUiState

    data class EmailVerificationPanel(
        override val isLoading: Boolean,
        val email: String,
        val password: String,
    ) : SignInUiState
}
