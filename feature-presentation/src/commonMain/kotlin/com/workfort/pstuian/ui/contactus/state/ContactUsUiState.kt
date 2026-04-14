package com.workfort.pstuian.ui.contactus.state

import com.workfort.pstuian.featuredomain.model.ContactUsInput
import com.workfort.pstuian.featuredomain.model.ContactUsInputValidationError

sealed interface ContactUsUiState {
    data object None : ContactUsUiState
    data class Content(
        val input: ContactUsInput = ContactUsInput.INITIAL,
        val validationError: ContactUsInputValidationError = ContactUsInputValidationError.INITIAL,
        val isLoading: Boolean = false,
    ) : ContactUsUiState
}
