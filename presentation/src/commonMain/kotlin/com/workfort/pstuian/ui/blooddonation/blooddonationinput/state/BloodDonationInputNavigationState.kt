package com.workfort.pstuian.ui.blooddonation.blooddonationinput.state

sealed interface BloodDonationInputNavigationState {
    data object GoBack : BloodDonationInputNavigationState
}