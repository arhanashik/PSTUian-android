package com.workfort.pstuian.ui.blooddonation.blooddonationhistory.state

import com.workfort.pstuian.featuredomain.model.UserType

sealed interface BloodDonationHistoryNavigationState {
    data object GoBack : BloodDonationHistoryNavigationState
    data class GoToCreateBloodDonation(val userId: Int, val userType: UserType) : BloodDonationHistoryNavigationState
    data class GoToEditBloodDonation(
        val donationId: Int,
        val userId: Int,
        val userType: UserType,
    ) : BloodDonationHistoryNavigationState
}
