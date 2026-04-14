package com.workfort.pstuian.ui.mycheckinlist.state

import com.workfort.pstuian.featuredomain.model.CheckInEntity
import com.workfort.pstuian.featuredomain.model.CheckInPrivacy

sealed interface MessageState {
    data class Success(val message: String) : MessageState
    data class Error(val message: String) : MessageState
    data class ShowDetails(val item: CheckInEntity) : MessageState
    data class ConfirmPrivacyChange(val item: CheckInEntity, val privacy: CheckInPrivacy) : MessageState
    data class ConfirmDelete(val item: CheckInEntity) : MessageState
}
