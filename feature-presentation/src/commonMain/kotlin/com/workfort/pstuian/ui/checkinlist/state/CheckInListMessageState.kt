package com.workfort.pstuian.ui.checkinlist.state

import com.workfort.pstuian.featuredomain.model.CheckInLocationEntity

sealed interface CheckInListMessageState {
    data class Error(val message: String) : CheckInListMessageState
    data class Success(val message: String) : CheckInListMessageState
    data class Call(val phoneNumber: String) : CheckInListMessageState
    data class ConfirmCheckIn(val location: CheckInLocationEntity) : CheckInListMessageState
}
