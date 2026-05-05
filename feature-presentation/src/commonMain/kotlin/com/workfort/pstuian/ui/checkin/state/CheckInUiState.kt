package com.workfort.pstuian.ui.checkin.state

import com.workfort.pstuian.featuredomain.model.CheckInLocation
import com.workfort.pstuian.ui.checkin.displaydata.CheckInDisplayData

sealed interface CheckInUiState {
    data object None : CheckInUiState
    data object Loading : CheckInUiState
    data class Content(
        val checkInLocations: List<CheckInLocation>,
        val selectedLocationId: Int,
        val currentUserCheckIn: CheckInDisplayData? = null,
        val otherCheckIns: List<CheckInDisplayData> = emptyList(),
        val isLocationListLoading: Boolean = false,
        val isCheckInLoading: Boolean = false,
    ) : CheckInUiState
    data class Error(val error: String? = null) : CheckInUiState
}
