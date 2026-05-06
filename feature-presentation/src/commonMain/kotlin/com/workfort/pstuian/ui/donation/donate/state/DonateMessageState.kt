package com.workfort.pstuian.ui.donation.donate.state

sealed interface DonateMessageState {
    data class Success(
        val cancelable: Boolean,
        val message: String,
        val onConfirm: () -> Unit,
    ) : DonateMessageState

    data class Error(val message: String) : DonateMessageState
}
