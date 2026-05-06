package com.workfort.pstuian.ui.donation.donationhistory.state

import com.workfort.pstuian.featuredomain.model.Donation

sealed interface DonationHistoryUiState {
    data object None : DonationHistoryUiState

    data class Content(
        val donations: List<Donation> = emptyList(),
        val isLoading: Boolean = false,
    ) : DonationHistoryUiState
}