package com.workfort.pstuian.app.ui.blooddonationrequestcreate

import com.workfort.pstuian.model.BloodDonationRequestInput

sealed class BloodDonationRequestCreateScreenUiEvent {
    data object None : BloodDonationRequestCreateScreenUiEvent()
    data object OnClickBack : BloodDonationRequestCreateScreenUiEvent()
    data object OnClickSelectDate : BloodDonationRequestCreateScreenUiEvent()
    data object OnClickSend : BloodDonationRequestCreateScreenUiEvent()
    data class OnChangeInput(
        val input: BloodDonationRequestInput,
    ) : BloodDonationRequestCreateScreenUiEvent()
    data class OnSelectDate(val dateMills: Long?) : BloodDonationRequestCreateScreenUiEvent()
    data object MessageConsumed : BloodDonationRequestCreateScreenUiEvent()
    data object NavigationConsumed : BloodDonationRequestCreateScreenUiEvent()
}