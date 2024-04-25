package com.workfort.pstuian.app.ui.mycheckinlist

import com.workfort.pstuian.model.CheckInEntity
import com.workfort.pstuian.model.CheckInPrivacy

sealed class MyCheckInListScreenUiEvent {
    data object None : MyCheckInListScreenUiEvent()
    data class OnLoadMoreData(val refresh: Boolean) : MyCheckInListScreenUiEvent()
    data object OnClickBack : MyCheckInListScreenUiEvent()
    data class OnClickItem(val item: CheckInEntity) : MyCheckInListScreenUiEvent()
    data class OnClickChangePrivacy(
        val item: CheckInEntity,
        val privacy: CheckInPrivacy,
    ) : MyCheckInListScreenUiEvent()
    data class OnClickDelete(val item: CheckInEntity) : MyCheckInListScreenUiEvent()
    data class OnChangePrivacy(
        val item: CheckInEntity,
        val privacy: CheckInPrivacy,
    ) : MyCheckInListScreenUiEvent()
    data class OnDelete(val item: CheckInEntity) : MyCheckInListScreenUiEvent()
    data object MessageConsumed : MyCheckInListScreenUiEvent()
    data object NavigationConsumed : MyCheckInListScreenUiEvent()
}