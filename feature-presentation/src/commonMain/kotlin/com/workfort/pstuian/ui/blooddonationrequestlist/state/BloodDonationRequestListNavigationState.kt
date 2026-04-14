package com.workfort.pstuian.ui.blooddonationrequestlist.state

sealed interface BloodDonationRequestListNavigationState {
    data object GoBack : BloodDonationRequestListNavigationState
    data object BloodDonationRequestCreateScreen : BloodDonationRequestListNavigationState
}
