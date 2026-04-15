package com.workfort.pstuian.ui.emailverification.state

import androidx.compose.runtime.Immutable
import com.workfort.pstuian.featuredomain.model.UserType

@Immutable
data class EmailVerificationUiState(
    val userType: UserType = UserType.STUDENT,
    val validationError: String? = null,
    val isLoading: Boolean = false,
)
