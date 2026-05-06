package com.workfort.pstuian.ui.donation.donate.state

import com.workfort.pstuian.featuredomain.model.DonationInput
import com.workfort.pstuian.featuredomain.model.DonationInputValidationError

data class DonateUiState(
    val isLoading: Boolean = false,
    val donationOption: String = "",
    val donationInput: DonationInput = DonationInput.INITIAL,
    val validationError: DonationInputValidationError = DonationInputValidationError.INITIAL,
)
