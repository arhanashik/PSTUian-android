package com.workfort.pstuian.ui.blooddonation.blooddonationinput.state

sealed interface BloodDonationInputUiState {
    val title: String

    data class None(override val title: String = "") : BloodDonationInputUiState

    data class Content(
        override val title: String,
        val requestId: Int = 0,
        val date: Long? = null,
        val formattedDate: String = "",
        val info: String = "",
        val enableSendButton: Boolean = false,
        val isOperationLoading: Boolean = false,
    ) : BloodDonationInputUiState
}