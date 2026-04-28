package com.workfort.pstuian.ui.checkinlist.state

import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.featuredomain.model.CheckInLocation

sealed interface CheckInListUiState {
    data object None : CheckInListUiState
    data class Loading(val message: String) : CheckInListUiState
    data class Error(val message: String) : CheckInListUiState
    data class Content(
        val checkInLocation: CheckInLocation? = null,
        val checkInList: List<CheckIn> = emptyList(),
        val isLoading: Boolean = false,
        val isOperationLoading: Boolean = false,
    ) : CheckInListUiState
}
