package com.workfort.pstuian.app.ui.common.locationpicker

import com.workfort.pstuian.view.service.StateReducer


class LocationPickerScreenStateReducer : StateReducer<LocationPickerScreenState, LocationPickerScreenStateUpdate> {
 override val initial: LocationPickerScreenState
  get() = LocationPickerScreenState()
}