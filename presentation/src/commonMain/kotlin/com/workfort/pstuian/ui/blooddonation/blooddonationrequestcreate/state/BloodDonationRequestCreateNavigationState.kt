package com.workfort.pstuian.ui.blooddonation.blooddonationrequestcreate.state

sealed interface BloodDonationRequestCreateNavigationState {
    data object GoBack : BloodDonationRequestCreateNavigationState
}
