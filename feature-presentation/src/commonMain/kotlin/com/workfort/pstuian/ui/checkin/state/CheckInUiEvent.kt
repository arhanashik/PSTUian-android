package com.workfort.pstuian.ui.checkin.state

import com.workfort.pstuian.ui.checkin.displaydata.CheckInDisplayData

sealed interface CheckInUiEvent {
    data object BackClicked : CheckInUiEvent
    data class CheckInItemClicked(val item: CheckInDisplayData) : CheckInUiEvent
    data class LocationSelected(val locationId: Int) : CheckInUiEvent
    data class CallClicked(val phoneNumber: String) : CheckInUiEvent
    data class CheckInClicked(val selectedLocationId: Int) : CheckInUiEvent
    data object OnLoadMoreLocations : CheckInUiEvent
    data class OnLoadMoreCheckIn(val locationId: Int) : CheckInUiEvent
}
