package com.workfort.pstuian.ui.locationpicker.state

sealed interface LocationPickerNavigationState {
    data class GoBack(
        val selectedLocationId: Int?,
    ) : LocationPickerNavigationState
}
