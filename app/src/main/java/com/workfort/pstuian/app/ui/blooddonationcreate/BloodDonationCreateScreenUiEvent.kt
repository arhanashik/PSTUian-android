package com.workfort.pstuian.app.ui.blooddonationcreate

import com.workfort.pstuian.model.BloodDonationInput

sealed class BloodDonationCreateScreenUiEvent {
    data object None : BloodDonationCreateScreenUiEvent()
    data object OnClickBack : BloodDonationCreateScreenUiEvent()
    data object OnClickSelectDate : BloodDonationCreateScreenUiEvent()
    data object OnClickSend : BloodDonationCreateScreenUiEvent()
    data class OnChangeInput(val input: BloodDonationInput) : BloodDonationCreateScreenUiEvent()
    data class OnSelectDate(val dateMills: Long?) : BloodDonationCreateScreenUiEvent()
    data object MessageConsumed : BloodDonationCreateScreenUiEvent()
    data object NavigationConsumed : BloodDonationCreateScreenUiEvent()
}