package com.workfort.pstuian.ui.blooddonationrequestcreate.state

import com.workfort.pstuian.featuredomain.model.BloodDonationRequestInput

sealed class BloodDonationRequestCreateUiEvent {
    data object BackClicked : BloodDonationRequestCreateUiEvent()
    data object SelectDateClicked : BloodDonationRequestCreateUiEvent()
    data class InputChanged(val input: BloodDonationRequestInput) : BloodDonationRequestCreateUiEvent()
    data object SendClicked : BloodDonationRequestCreateUiEvent()
}
