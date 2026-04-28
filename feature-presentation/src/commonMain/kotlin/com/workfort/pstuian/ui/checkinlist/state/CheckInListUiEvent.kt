package com.workfort.pstuian.ui.checkinlist.state

import com.workfort.pstuian.featuredomain.model.CheckIn

sealed interface CheckInListUiEvent {
    data object OnClickBack : CheckInListUiEvent
    data class OnClickItem(val item: CheckIn) : CheckInListUiEvent
    data class OnClickCall(val phoneNumber: String) : CheckInListUiEvent
    data class OnSelectLocation(val locationId: Int) : CheckInListUiEvent
    data object OnClickCheckIn : CheckInListUiEvent
    data object OnLoadMoreLocations : CheckInListUiEvent
    data object OnLoadMore : CheckInListUiEvent
}
