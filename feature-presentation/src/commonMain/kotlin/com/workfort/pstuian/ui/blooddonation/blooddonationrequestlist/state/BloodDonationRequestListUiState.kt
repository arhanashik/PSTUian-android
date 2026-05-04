package com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.state

import com.workfort.pstuian.featuredomain.model.BloodDonationRequest

sealed interface BloodDonationRequestListUiState {
    object None : BloodDonationRequestListUiState

    data class Content(
        val requestList: List<BloodDonationRequest> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
    ) : BloodDonationRequestListUiState
}
