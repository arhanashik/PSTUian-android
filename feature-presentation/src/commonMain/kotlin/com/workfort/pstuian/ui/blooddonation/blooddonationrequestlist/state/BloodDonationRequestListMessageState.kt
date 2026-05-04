package com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.state

import com.workfort.pstuian.featuredomain.model.BloodDonationRequestEntity

sealed interface BloodDonationRequestListMessageState {
    data class ShowDetails(val item: BloodDonationRequestEntity) : BloodDonationRequestListMessageState
    data class Call(val phoneNumber: String) : BloodDonationRequestListMessageState
}
