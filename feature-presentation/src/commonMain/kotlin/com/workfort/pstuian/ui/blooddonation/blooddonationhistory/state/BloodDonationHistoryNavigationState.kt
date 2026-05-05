package com.workfort.pstuian.ui.blooddonation.blooddonationhistory.state

sealed interface BloodDonationHistoryNavigationState {
    data object GoBack : BloodDonationHistoryNavigationState
    data object GoToCreateBloodDonation : BloodDonationHistoryNavigationState
    data class GoToEditBloodDonation(
        val donationId: Int,
    ) : BloodDonationHistoryNavigationState
}
