package com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.state

import com.workfort.pstuian.featuredomain.model.BloodDonationRequest

sealed interface BloodDonationRequestListMessageState {
    data class ShowDetails(val item: BloodDonationRequest) : BloodDonationRequestListMessageState
    data class Call(val phoneNumber: String) : BloodDonationRequestListMessageState
    data class Confirm(val message: String, val onConfirm: () -> Unit) : BloodDonationRequestListMessageState
    data class Snackbar(val message: String) : BloodDonationRequestListMessageState
}
