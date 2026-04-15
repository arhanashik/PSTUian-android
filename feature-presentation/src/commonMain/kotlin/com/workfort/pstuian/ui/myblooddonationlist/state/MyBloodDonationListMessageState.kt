package com.workfort.pstuian.ui.myblooddonationlist.state

sealed interface MyBloodDonationListMessageState {
    data class Loading(val cancelable: Boolean) : MyBloodDonationListMessageState
    data class ConfirmDelete(val onConfirm: () -> Unit) : MyBloodDonationListMessageState
    data class Success(val message: String) : MyBloodDonationListMessageState
    data class Error(val message: String) : MyBloodDonationListMessageState
}
