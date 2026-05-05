package com.workfort.pstuian.ui.blooddonation.blooddonationhistory.state

sealed interface BloodDonationHistoryMessageState {
    data class ConfirmDelete(val onConfirm: () -> Unit) : BloodDonationHistoryMessageState
    data class Success(val message: String) : BloodDonationHistoryMessageState
    data class Error(val message: String) : BloodDonationHistoryMessageState
}
