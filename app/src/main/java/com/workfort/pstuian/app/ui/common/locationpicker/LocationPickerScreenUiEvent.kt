package com.workfort.pstuian.app.ui.common.locationpicker

import com.workfort.pstuian.model.CheckInLocationEntity

sealed class LocationPickerScreenUiEvent {
    data object None : LocationPickerScreenUiEvent()
    data class OnSearch(
        val query: String,
        val refresh: Boolean,
    ) : LocationPickerScreenUiEvent()
    data class OnAddLocation(val locationName: String) : LocationPickerScreenUiEvent()
    data object OnClickBack : LocationPickerScreenUiEvent()
    data object OnClickAddLocation : LocationPickerScreenUiEvent()
    data class OnClickLocation(val location: CheckInLocationEntity) : LocationPickerScreenUiEvent()
    data object MessageConsumed : LocationPickerScreenUiEvent()
    data object NavigationConsumed : LocationPickerScreenUiEvent()
}