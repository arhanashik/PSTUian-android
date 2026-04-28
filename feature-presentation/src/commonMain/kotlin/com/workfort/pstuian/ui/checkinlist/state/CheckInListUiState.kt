package com.workfort.pstuian.ui.checkinlist.state

import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.featuredomain.model.CheckInLocation

sealed interface CheckInListUiState {
    data object None : CheckInListUiState
    data object Loading : CheckInListUiState
    data class Content(
        val checkInLocation: CheckInLocation,
        val checkInList: List<CheckIn> = emptyList(),
        val isLoading: Boolean = false,
    ) : CheckInListUiState
    data class Error(val error: String? = null) : CheckInListUiState
}
