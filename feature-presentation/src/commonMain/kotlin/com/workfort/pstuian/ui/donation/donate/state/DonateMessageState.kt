package com.workfort.pstuian.ui.donation.donate.state

sealed interface DonateMessageState {
    data class ShowAlert(val title: String, val message: String) : DonateMessageState
    data class Error(val message: String) : DonateMessageState
}
