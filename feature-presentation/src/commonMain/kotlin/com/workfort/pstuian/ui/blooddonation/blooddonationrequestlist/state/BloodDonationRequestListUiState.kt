package com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.state

import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.screendata.BloodDonationRequestDisplayData

sealed interface BloodDonationRequestListUiState {
    object None : BloodDonationRequestListUiState

    data class Content(
        val requestList: List<BloodDonationRequestDisplayData> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
    ) : BloodDonationRequestListUiState
}
