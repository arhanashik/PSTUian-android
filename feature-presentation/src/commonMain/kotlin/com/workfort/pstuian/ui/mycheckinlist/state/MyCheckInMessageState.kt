package com.workfort.pstuian.ui.mycheckinlist.state

import com.workfort.pstuian.featuredomain.model.CheckInEntity
import com.workfort.pstuian.featuredomain.model.CheckInPrivacy

sealed interface MyCheckInMessageState {
    data class Success(val message: String) : MyCheckInMessageState
    data class Error(val message: String) : MyCheckInMessageState
    data class ShowDetails(val item: CheckInEntity) : MyCheckInMessageState
    data class ConfirmPrivacyChange(val item: CheckInEntity, val privacy: CheckInPrivacy) : MyCheckInMessageState
    data class ConfirmDelete(val item: CheckInEntity) : MyCheckInMessageState
}
