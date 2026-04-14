package com.workfort.pstuian.ui.blooddonationcreate.state

sealed class BloodDonationCreateUiEvent {
    data object BackClicked : BloodDonationCreateUiEvent()
    data class RequestIdChanged(val requestId: Int) : BloodDonationCreateUiEvent()
    data object SelectDateClicked : BloodDonationCreateUiEvent()
    data class InfoChanged(val info: String) : BloodDonationCreateUiEvent()
    data object SendClicked : BloodDonationCreateUiEvent()
}