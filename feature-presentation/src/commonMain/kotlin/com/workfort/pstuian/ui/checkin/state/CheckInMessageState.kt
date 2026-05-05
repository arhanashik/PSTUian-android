package com.workfort.pstuian.ui.checkin.state

import com.workfort.pstuian.featuredomain.model.CheckInLocation

sealed interface CheckInMessageState {
    data class Loading(val cancelable: Boolean = false) : CheckInMessageState
    data class Success(val message: String) : CheckInMessageState
    data class Error(val message: String) : CheckInMessageState
    data class Call(val phoneNumber: String) : CheckInMessageState
    data class ConfirmCheckIn(val location: CheckInLocation, val onConfirm: () -> Unit) : CheckInMessageState
    data class CheckInLocationSelection(
        val locations: List<CheckInLocation>,
        val selectedLocationId: Int?,
        val onSelect: (CheckInLocation) -> Unit,
    ) : CheckInMessageState
    data class ShowSnackBar(val message: String) : CheckInMessageState
}
