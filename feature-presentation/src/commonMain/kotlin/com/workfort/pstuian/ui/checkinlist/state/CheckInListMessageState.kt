package com.workfort.pstuian.ui.checkinlist.state

import com.workfort.pstuian.featuredomain.model.CheckInLocation

sealed interface CheckInListMessageState {
    data class Loading(val cancelable: Boolean = false) : CheckInListMessageState
    data class Success(val message: String) : CheckInListMessageState
    data class Error(val message: String) : CheckInListMessageState
    data class Call(val phoneNumber: String) : CheckInListMessageState
    data class ConfirmCheckIn(val location: CheckInLocation, val onConfirm: () -> Unit) : CheckInListMessageState
    data class CheckInLocationSelection(
        val locations: List<CheckInLocation>,
        val selectedLocationId: Int?,
        val onSelect: (CheckInLocation) -> Unit,
    ) : CheckInListMessageState
    data class ShowSnackBar(val message: String) : CheckInListMessageState
}
