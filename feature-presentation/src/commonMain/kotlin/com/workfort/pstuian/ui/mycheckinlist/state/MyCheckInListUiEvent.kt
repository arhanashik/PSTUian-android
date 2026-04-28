package com.workfort.pstuian.ui.mycheckinlist.state

import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.featuredomain.model.CheckInPrivacy

sealed interface MyCheckInListUiEvent {
    data class LoadMoreData(val refresh: Boolean) : MyCheckInListUiEvent
    data object BackClicked : MyCheckInListUiEvent
    data class ItemClicked(val item: CheckIn) : MyCheckInListUiEvent
    data class ChangePrivacy(val item: CheckIn, val privacy: CheckInPrivacy) : MyCheckInListUiEvent
    data class Delete(val item: CheckIn) : MyCheckInListUiEvent
}
