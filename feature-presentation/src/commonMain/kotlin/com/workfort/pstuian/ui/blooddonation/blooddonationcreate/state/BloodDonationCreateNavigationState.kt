package com.workfort.pstuian.ui.blooddonation.blooddonationcreate.state

sealed interface BloodDonationCreateNavigationState {
    data object GoBack : BloodDonationCreateNavigationState
}