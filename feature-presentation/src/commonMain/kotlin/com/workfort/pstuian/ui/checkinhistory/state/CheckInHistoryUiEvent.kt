package com.workfort.pstuian.ui.checkinhistory.state

import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.featuredomain.model.CheckInPrivacy

sealed interface CheckInHistoryUiEvent {
    data object BackClicked : CheckInHistoryUiEvent
    data object LoadMore : CheckInHistoryUiEvent
    data class ItemClicked(val item: CheckIn) : CheckInHistoryUiEvent
    data class ChangePrivacy(val item: CheckIn, val privacy: CheckInPrivacy) : CheckInHistoryUiEvent
    data class Delete(val item: CheckIn) : CheckInHistoryUiEvent
}
