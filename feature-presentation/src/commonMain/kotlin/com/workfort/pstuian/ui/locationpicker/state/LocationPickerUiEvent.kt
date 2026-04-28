package com.workfort.pstuian.ui.locationpicker.state

import com.workfort.pstuian.featuredomain.model.CheckInLocation

sealed interface LocationPickerUiEvent {
    data class OnSearch(val query: String, val refresh: Boolean) : LocationPickerUiEvent
    data class OnAddLocation(val locationName: String) : LocationPickerUiEvent
    data object OnClickBack : LocationPickerUiEvent
    data object OnClickAddLocation : LocationPickerUiEvent
    data class OnClickLocation(val location: CheckInLocation) : LocationPickerUiEvent
    data object MessageConsumed : LocationPickerUiEvent
    data object NavigationConsumed : LocationPickerUiEvent
}
