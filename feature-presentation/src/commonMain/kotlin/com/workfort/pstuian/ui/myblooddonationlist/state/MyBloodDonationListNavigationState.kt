package com.workfort.pstuian.ui.myblooddonationlist.state

sealed interface MyBloodDonationListNavigationState {
    data object GoBack : MyBloodDonationListNavigationState
    data object GoToCreateBloodDonationRequest : MyBloodDonationListNavigationState
    data class GoToEditBloodDonationRequest(
        val donationId: Int,
    ) : MyBloodDonationListNavigationState
}
