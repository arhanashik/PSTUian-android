package com.workfort.pstuian.ui.blooddonationcreate.state

sealed interface BloodDonationCreateMessageState {
    data class SelectDate(
        val allowedDateTill: Long,
        val onSelect: (Long?) -> Unit,
    ) : BloodDonationCreateMessageState
    data class Error(val message: String) : BloodDonationCreateMessageState
    data class Snackbar(val message: String) : BloodDonationCreateMessageState
}