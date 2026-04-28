package com.workfort.pstuian.ui.mycheckinlist.state

import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.featuredomain.model.CheckInPrivacy

sealed interface MyCheckInMessageState {
    data class Success(val message: String) : MyCheckInMessageState
    data class Error(val message: String) : MyCheckInMessageState
    data class ShowDetails(
        val item: CheckIn,
        val onClickChangePrivacy: (CheckInPrivacy) -> Unit,
        val onClickDelete: () -> Unit,
    ) : MyCheckInMessageState
    data class ConfirmPrivacyChange(
        val item: CheckIn,
        val privacy: CheckInPrivacy,
        val onConfirm: () -> Unit,
    ) : MyCheckInMessageState
    data class ConfirmDelete(
        val item: CheckIn,
        val onConfirm: () -> Unit,
    ) : MyCheckInMessageState
}
