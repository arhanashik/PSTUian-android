package com.workfort.pstuian.ui.donors.state

sealed interface DonorsNavigationState {
    data object GoBack : DonorsNavigationState
    data object DonateScreen : DonorsNavigationState
}
