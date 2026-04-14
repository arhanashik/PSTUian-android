package com.workfort.pstuian.ui.donate.state

sealed interface DonateNavigationState {
    data object GoBack : DonateNavigationState
}
