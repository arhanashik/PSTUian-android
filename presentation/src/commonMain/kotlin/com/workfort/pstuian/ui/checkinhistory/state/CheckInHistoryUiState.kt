package com.workfort.pstuian.ui.checkinhistory.state

import com.workfort.pstuian.featuredomain.model.CheckIn

sealed interface CheckInHistoryUiState {

    data object None : CheckInHistoryUiState

    data class Content(
        val isOperationLoading: Boolean = false,
        val checkIns: List<CheckIn> = emptyList(),
        val isContentLoading: Boolean = false,
        val error: String? = null,
    ) : CheckInHistoryUiState
}
