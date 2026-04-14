package com.workfort.pstuian.ui.blooddonationcreate.state

sealed interface BloodDonationCreateNavigationState {
    data object GoBack : BloodDonationCreateNavigationState
}