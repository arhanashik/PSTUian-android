package com.workfort.pstuian.ui.forgotpassword.state

import com.workfort.pstuian.featuredomain.model.UserType

data class ForgotPasswordUiState(
    val isLoading: Boolean = false,
    val userType: UserType? = null,
    val validationError: String? = null,
)
