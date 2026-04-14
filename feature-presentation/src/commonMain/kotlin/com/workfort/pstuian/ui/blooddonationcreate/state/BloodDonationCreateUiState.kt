package com.workfort.pstuian.ui.blooddonationcreate.state

sealed interface BloodDonationCreateUiState {
    object None : BloodDonationCreateUiState

    data class Content(
        val requestId: Int = 0,
        val date: Long? = 0,
        val formattedDate: String = "",
        val info: String = "",
        val enableSendButton: Boolean = false,
        val isOperationLoading: Boolean = false,
    ) : BloodDonationCreateUiState
}