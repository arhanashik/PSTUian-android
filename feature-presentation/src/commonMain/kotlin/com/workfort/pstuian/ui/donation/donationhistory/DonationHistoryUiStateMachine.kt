package com.workfort.pstuian.ui.donation.donationhistory

import com.workfort.pstuian.featuredomain.model.Donation
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.donation.donationhistory.state.DonationHistoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DonationHistoryUiStateMachine : UiStateMachine<DonationHistoryUiState> {
    private val _uiState = MutableStateFlow<DonationHistoryUiState>(DonationHistoryUiState.None)
    override val uiState: StateFlow<DonationHistoryUiState> = _uiState.asStateFlow()

    fun showLoading(isLoading: Boolean) {
        _uiState.update { current ->
            when (current) {
                DonationHistoryUiState.None -> DonationHistoryUiState.Content(isLoading = isLoading)
                is DonationHistoryUiState.Content -> current.copy(isLoading = isLoading)
            }
        }
    }

    fun showDoners(donations: List<Donation>) {
        _uiState.update { current ->
            when (current) {
                DonationHistoryUiState.None -> DonationHistoryUiState.Content(donations = donations, isLoading = false)
                is DonationHistoryUiState.Content -> current.copy(donations = donations, isLoading = false)
            }
        }
    }
}
