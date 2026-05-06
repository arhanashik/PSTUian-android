package com.workfort.pstuian.ui.donation.donors.state

import com.workfort.pstuian.featuredomain.model.Donor

sealed interface DonorsUiState {
    data object None : DonorsUiState

    data class Content(
        val donors: List<Donor> = emptyList(),
        val isLoading: Boolean = false,
    ) : DonorsUiState
}