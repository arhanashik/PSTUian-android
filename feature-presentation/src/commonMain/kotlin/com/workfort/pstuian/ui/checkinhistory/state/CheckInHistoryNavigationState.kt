package com.workfort.pstuian.ui.checkinhistory.state

sealed interface CheckInHistoryNavigationState {
    data object GoBack : CheckInHistoryNavigationState
}
