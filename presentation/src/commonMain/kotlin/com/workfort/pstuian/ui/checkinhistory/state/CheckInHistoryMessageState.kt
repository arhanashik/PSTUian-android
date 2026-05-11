package com.workfort.pstuian.ui.checkinhistory.state

import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.featuredomain.model.CheckInPrivacy

sealed interface CheckInHistoryMessageState {
    data class Success(val message: String) : CheckInHistoryMessageState
    data class Error(val message: String) : CheckInHistoryMessageState
    data class ShowDetails(
        val item: CheckIn,
        val onClickChangePrivacy: (CheckInPrivacy) -> Unit,
        val onClickDelete: () -> Unit,
    ) : CheckInHistoryMessageState
    data class ConfirmPrivacyChange(
        val item: CheckIn,
        val privacy: CheckInPrivacy,
        val onConfirm: () -> Unit,
    ) : CheckInHistoryMessageState
    data class ConfirmDelete(
        val item: CheckIn,
        val onConfirm: () -> Unit,
    ) : CheckInHistoryMessageState
}
