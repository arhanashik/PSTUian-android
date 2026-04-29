package com.workfort.pstuian.ui.checkinlist.state

import com.workfort.pstuian.featuredomain.model.CheckInLocation
import com.workfort.pstuian.ui.checkinlist.displaydata.CheckInDisplayData

sealed interface CheckInListUiState {
    data object None : CheckInListUiState
    data object Loading : CheckInListUiState
    data class Content(
        val checkInLocations: List<CheckInLocation>,
        val selectedLocationId: Int,
        val currentUserCheckIn: CheckInDisplayData? = null,
        val otherCheckIns: List<CheckInDisplayData> = emptyList(),
        val isLocationListLoading: Boolean = false,
        val isCheckInListLoading: Boolean = false,
    ) : CheckInListUiState
    data class Error(val error: String? = null) : CheckInListUiState
}
