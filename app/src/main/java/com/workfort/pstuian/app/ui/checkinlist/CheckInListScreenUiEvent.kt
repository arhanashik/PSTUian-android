package com.workfort.pstuian.app.ui.checkinlist

import com.workfort.pstuian.model.CheckInEntity

sealed class CheckInListScreenUiEvent {
    data object None : CheckInListScreenUiEvent()
    data class OnLoadCheckInListList(val isRefresh: Boolean) : CheckInListScreenUiEvent()
    data object OnClickBack : CheckInListScreenUiEvent()
    data class OnClickItem(val item: CheckInEntity) : CheckInListScreenUiEvent()
    data class OnClickCall(val phoneNumber: String) : CheckInListScreenUiEvent()
    data object OnClickChangeLocation : CheckInListScreenUiEvent()
    data object OnClickCheckInList : CheckInListScreenUiEvent()
    data class OnCall(val phoneNumber: String) : CheckInListScreenUiEvent()
    data class OnCheckIn(val locationId: Int) : CheckInListScreenUiEvent()
    data object MessageConsumed : CheckInListScreenUiEvent()
    data object NavigationConsumed : CheckInListScreenUiEvent()
}