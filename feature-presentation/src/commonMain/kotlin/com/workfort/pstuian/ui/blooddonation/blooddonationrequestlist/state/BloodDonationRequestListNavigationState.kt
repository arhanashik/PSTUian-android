package com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.state

sealed interface BloodDonationRequestListNavigationState {
    data object GoBack : BloodDonationRequestListNavigationState
    data object BloodDonationRequestCreateScreen : BloodDonationRequestListNavigationState
}
