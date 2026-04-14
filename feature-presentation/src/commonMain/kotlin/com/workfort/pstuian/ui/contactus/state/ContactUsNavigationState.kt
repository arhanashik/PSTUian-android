package com.workfort.pstuian.ui.contactus.state

sealed interface ContactUsNavigationState {
    data object GoBack : ContactUsNavigationState
}
