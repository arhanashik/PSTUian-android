package com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate.state

import com.workfort.pstuian.featuredomain.model.BloodDonationRequestInput
import com.workfort.pstuian.featuredomain.model.BloodDonationRequestInputError

sealed interface BloodDonationRequestCreateUiState {
    object None : BloodDonationRequestCreateUiState

    data class Content(
        val input: BloodDonationRequestInput = BloodDonationRequestInput.INITIAL,
        val validationError: BloodDonationRequestInputError = BloodDonationRequestInputError.INITIAL,
        val isOperationLoading: Boolean = false,
    ) : BloodDonationRequestCreateUiState
}
