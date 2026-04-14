package com.workfort.pstuian.ui.blooddonationrequestcreate.state

sealed interface BloodDonationRequestCreateMessageState {
    data class SelectDate(
        val allowedDateFrom: Long,
        val onSelect: (Long?) -> Unit,
    ) : BloodDonationRequestCreateMessageState
    data class Error(val message: String) : BloodDonationRequestCreateMessageState
    data class Snackbar(val message: String) : BloodDonationRequestCreateMessageState
}
