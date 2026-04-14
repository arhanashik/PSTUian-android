package com.workfort.pstuian.ui.blooddonationrequestcreate.state

sealed interface BloodDonationRequestCreateNavigationState {
    data object GoBack : BloodDonationRequestCreateNavigationState
}
