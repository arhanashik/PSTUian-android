package com.workfort.pstuian.ui.blooddonationrequestlist.state

import com.workfort.pstuian.featuredomain.model.BloodDonationRequestEntity

sealed interface BloodDonationRequestListUiState {
    object None : BloodDonationRequestListUiState

    data class Content(
        val requestList: List<BloodDonationRequestEntity> = emptyList(),
        val isLoading: Boolean = false,
        val loadError: String? = null,
    ) : BloodDonationRequestListUiState
}
