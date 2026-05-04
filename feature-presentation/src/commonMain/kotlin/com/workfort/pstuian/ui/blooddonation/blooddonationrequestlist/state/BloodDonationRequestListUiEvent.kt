package com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.state

import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.screendata.BloodDonationRequestDisplayData

sealed class BloodDonationRequestListUiEvent {
    data object BackClicked : BloodDonationRequestListUiEvent()
    data object CreateRequestClicked : BloodDonationRequestListUiEvent()
    data class ItemClicked(val item: BloodDonationRequestDisplayData) : BloodDonationRequestListUiEvent()
    data class CallClicked(val phoneNumber: String) : BloodDonationRequestListUiEvent()
    data class MarkAsCompleteClicked(val item: BloodDonationRequestDisplayData) : BloodDonationRequestListUiEvent()
    data object LoadMore : BloodDonationRequestListUiEvent()
}
