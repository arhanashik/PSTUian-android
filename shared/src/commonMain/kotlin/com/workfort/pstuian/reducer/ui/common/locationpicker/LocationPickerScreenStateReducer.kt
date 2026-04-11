package com.workfort.pstuian.reducer.ui.common.locationpicker

import com.workfort.pstuian.reducer.service.StateReducer


class LocationPickerScreenStateReducer : StateReducer<LocationPickerScreenState, LocationPickerScreenStateUpdate> {
 override val initial: LocationPickerScreenState
  get() = LocationPickerScreenState()
}