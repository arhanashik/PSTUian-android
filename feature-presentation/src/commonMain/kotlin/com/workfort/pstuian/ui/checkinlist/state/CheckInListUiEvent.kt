package com.workfort.pstuian.ui.checkinlist.state

import com.workfort.pstuian.ui.checkinlist.displaydata.CheckInDisplayData

sealed interface CheckInListUiEvent {
    data object OnClickBack : CheckInListUiEvent
    data class OnClickCheckInItem(val item: CheckInDisplayData) : CheckInListUiEvent
    data class OnSelectLocation(val locationId: Int) : CheckInListUiEvent
    data class OnClickCall(val phoneNumber: String) : CheckInListUiEvent
    data object OnClickCheckIn : CheckInListUiEvent
    data object OnLoadMoreLocations : CheckInListUiEvent
    data object OnLoadMore : CheckInListUiEvent
}
