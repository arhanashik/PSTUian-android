package com.workfort.pstuian.ui.checkinlist.state

import com.workfort.pstuian.featuredomain.model.CheckInEntity

sealed interface CheckInListUiEvent {
    data object OnClickBack : CheckInListUiEvent
    data class OnClickItem(val item: CheckInEntity) : CheckInListUiEvent
    data class OnClickCall(val phoneNumber: String) : CheckInListUiEvent
    data object OnClickChangeLocation : CheckInListUiEvent
    data object OnClickCheckIn : CheckInListUiEvent
    data object OnRefresh : CheckInListUiEvent
    data object OnLoadMore : CheckInListUiEvent
}
