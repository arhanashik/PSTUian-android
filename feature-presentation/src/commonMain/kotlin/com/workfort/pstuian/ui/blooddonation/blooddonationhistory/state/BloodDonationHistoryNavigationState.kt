package com.workfort.pstuian.ui.blooddonation.blooddonationhistory.state

sealed interface BloodDonationHistoryNavigationState {
    data object GoBack : BloodDonationHistoryNavigationState
    data object GoToCreateBloodDonationRequest : BloodDonationHistoryNavigationState
    data class GoToEditBloodDonationRequest(
        val donationId: Int,
    ) : BloodDonationHistoryNavigationState
}
