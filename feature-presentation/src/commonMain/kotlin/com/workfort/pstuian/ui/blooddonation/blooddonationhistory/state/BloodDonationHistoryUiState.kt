package com.workfort.pstuian.ui.blooddonation.blooddonationhistory.state

import com.workfort.pstuian.featuredomain.model.BloodDonationEntity

sealed interface BloodDonationHistoryUiState {

    data object None : BloodDonationHistoryUiState

    data class Content(
        val isOperationLoading: Boolean = false,
        val donations: List<BloodDonationEntity> = emptyList(),
        val isContentLoading: Boolean = false,
        val error: String? = null,
    ) : BloodDonationHistoryUiState
}
