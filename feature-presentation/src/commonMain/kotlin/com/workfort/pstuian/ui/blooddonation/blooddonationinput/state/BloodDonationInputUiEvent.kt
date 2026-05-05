package com.workfort.pstuian.ui.blooddonation.blooddonationinput.state

sealed class BloodDonationInputUiEvent {
    data object BackClicked : BloodDonationInputUiEvent()
    data class RequestIdChanged(val requestId: Int) : BloodDonationInputUiEvent()
    data object SelectDateClicked : BloodDonationInputUiEvent()
    data class InfoChanged(val info: String) : BloodDonationInputUiEvent()
    data class SendClicked(val input: BloodDonationInputData) : BloodDonationInputUiEvent()
}