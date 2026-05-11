package com.workfort.pstuian.ui.blooddonation.blooddonationinput.state

sealed interface BloodDonationInputMessageState {
    data class SelectDate(
        val allowedDateTill: Long,
        val onSelect: (Long?) -> Unit,
    ) : BloodDonationInputMessageState
    data class Error(val message: String) : BloodDonationInputMessageState
    data class Snackbar(val message: String) : BloodDonationInputMessageState
}