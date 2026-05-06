package com.workfort.pstuian.ui.donation.donate

import com.workfort.pstuian.featuredomain.model.DonationInput
import com.workfort.pstuian.featuredomain.model.DonationInputValidationError
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.donation.donate.state.DonateUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DonateUiStateMachine : UiStateMachine<DonateUiState> {

    private val _uiState = MutableStateFlow(DonateUiState())
    override val uiState: StateFlow<DonateUiState> = _uiState.asStateFlow()

    fun showLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun setDonationOption(option: String) {
        _uiState.update { it.copy(donationOption = option) }
    }

    fun setDonationInput(input: DonationInput) {
        _uiState.update { it.copy(donationInput = input) }
    }

    fun setValidationError(validationError: DonationInputValidationError) {
        _uiState.update { it.copy(validationError = validationError) }
    }
}
