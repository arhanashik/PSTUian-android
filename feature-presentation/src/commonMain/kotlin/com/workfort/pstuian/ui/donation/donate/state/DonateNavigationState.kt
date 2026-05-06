package com.workfort.pstuian.ui.donation.donate.state

sealed interface DonateNavigationState {
    data object GoBack : DonateNavigationState
}
