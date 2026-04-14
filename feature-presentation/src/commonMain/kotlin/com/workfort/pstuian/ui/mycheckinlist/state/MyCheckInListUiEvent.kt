package com.workfort.pstuian.ui.mycheckinlist.state

import com.workfort.pstuian.featuredomain.model.CheckInEntity
import com.workfort.pstuian.featuredomain.model.CheckInPrivacy

sealed interface MyCheckInListUiEvent {
    data class LoadMoreData(val refresh: Boolean) : MyCheckInListUiEvent
    data object ClickBack : MyCheckInListUiEvent
    data class ClickItem(val item: CheckInEntity) : MyCheckInListUiEvent
    data class ClickChangePrivacy(val item: CheckInEntity, val privacy: CheckInPrivacy) : MyCheckInListUiEvent
    data class ClickDelete(val item: CheckInEntity) : MyCheckInListUiEvent
    data class ChangePrivacy(val item: CheckInEntity, val privacy: CheckInPrivacy) : MyCheckInListUiEvent
    data class Delete(val item: CheckInEntity) : MyCheckInListUiEvent
    data object MessageConsumed : MyCheckInListUiEvent
    data object NavigationConsumed : MyCheckInListUiEvent
}
