package com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.state

import com.workfort.pstuian.featuredomain.model.BloodDonationRequestEntity

sealed class BloodDonationRequestListUiEvent {
    data object BackClicked : BloodDonationRequestListUiEvent()
    data object CreateRequestClicked : BloodDonationRequestListUiEvent()
    data class ItemClicked(val item: BloodDonationRequestEntity) : BloodDonationRequestListUiEvent()
    data class CallClicked(val phoneNumber: String) : BloodDonationRequestListUiEvent()
    data object LoadMore : BloodDonationRequestListUiEvent()
}
