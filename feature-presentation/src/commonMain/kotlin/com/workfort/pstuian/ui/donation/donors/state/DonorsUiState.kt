package com.workfort.pstuian.ui.donation.donors.state

import com.workfort.pstuian.featuredomain.model.Donor

sealed interface DonorsUiState {
    data object None : DonorsUiState
    data object Loading : DonorsUiState
    data class Content(
        val donorList: List<Donor>,
        val isLoading: Boolean = false,
    ) : DonorsUiState
}