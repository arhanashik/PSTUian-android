package com.workfort.pstuian.ui.blooddonation.blooddonationinput.state

sealed class BloodDonationInputUiEvent {
    data object BackClicked : BloodDonationInputUiEvent()
    data class InputChanged(val input: BloodDonationInputData) : BloodDonationInputUiEvent()
    data class SelectDateClicked(val input: BloodDonationInputData) : BloodDonationInputUiEvent()
    data class SendClicked(val input: BloodDonationInputData) : BloodDonationInputUiEvent()
}