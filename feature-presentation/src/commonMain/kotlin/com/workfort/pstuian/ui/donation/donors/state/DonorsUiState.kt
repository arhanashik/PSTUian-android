package com.workfort.pstuian.ui.donation.donors.state

import com.workfort.pstuian.featuredomain.model.Donation

sealed interface DonorsUiState {
    data object None : DonorsUiState

    data class Content(
        val donations: List<Donation> = emptyList(),
        val isLoading: Boolean = false,
    ) : DonorsUiState
}