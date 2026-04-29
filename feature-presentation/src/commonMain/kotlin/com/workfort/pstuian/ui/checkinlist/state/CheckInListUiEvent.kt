package com.workfort.pstuian.ui.checkinlist.state

import com.workfort.pstuian.ui.checkinlist.displaydata.CheckInDisplayData

sealed interface CheckInListUiEvent {
    data object BackClicked : CheckInListUiEvent
    data class CheckInItemClicked(val item: CheckInDisplayData) : CheckInListUiEvent
    data class LocationSelected(val locationId: Int) : CheckInListUiEvent
    data class CallClicked(val phoneNumber: String) : CheckInListUiEvent
    data object CheckInClicked : CheckInListUiEvent
    data object OnLoadMoreLocations : CheckInListUiEvent
    data class OnLoadMoreCheckIn(val locationId: Int) : CheckInListUiEvent
}
