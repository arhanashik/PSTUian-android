package com.workfort.pstuian.ui.blooddonation.blooddonationinput.state

sealed interface BloodDonationInputUiState {
    val title: String

    data class None(override val title: String = "") : BloodDonationInputUiState

    data class Content(
        override val title: String,
        val inputData: BloodDonationInputData = BloodDonationInputData(),
        val enableSendButton: Boolean = false,
        val isOperationLoading: Boolean = false,
    ) : BloodDonationInputUiState
}

data class BloodDonationInputData(
    val requestId: Int = 0,
    val formattedDate: String = "",
    val info: String = "",
) {
    fun hasError() = formattedDate.isBlank() || info.isBlank()
}