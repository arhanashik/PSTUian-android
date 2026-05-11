package com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.state

import com.workfort.pstuian.featuredomain.model.UserType

sealed interface BloodDonationRequestListNavigationState {
    data object GoBack : BloodDonationRequestListNavigationState
    data object BloodDonationRequestCreateScreen : BloodDonationRequestListNavigationState
    data class BloodDonationCreateScreen(
        val requestId: Int,
        val userId: Int,
        val userType: UserType,
    ) : BloodDonationRequestListNavigationState
}
