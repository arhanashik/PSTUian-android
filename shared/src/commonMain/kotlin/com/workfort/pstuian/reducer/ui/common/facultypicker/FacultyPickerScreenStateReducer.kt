package com.workfort.pstuian.reducer.ui.common.facultypicker

import com.workfort.pstuian.reducer.service.StateReducer


class FacultyPickerScreenStateReducer : StateReducer<FacultyPickerScreenState, FacultyPickerScreenStateUpdate> {
 override val initial: FacultyPickerScreenState
  get() = FacultyPickerScreenState()
}