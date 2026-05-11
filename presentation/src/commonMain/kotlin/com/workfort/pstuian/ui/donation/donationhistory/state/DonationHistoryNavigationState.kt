package com.workfort.pstuian.ui.donation.donationhistory.state

sealed interface DonationHistoryNavigationState {
    data object GoBack : DonationHistoryNavigationState
    data object DonateScreen : DonationHistoryNavigationState
}
