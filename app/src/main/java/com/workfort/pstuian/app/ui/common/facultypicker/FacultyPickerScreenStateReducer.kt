package com.workfort.pstuian.app.ui.common.facultypicker

import com.workfort.pstuian.view.service.StateReducer


class FacultyPickerScreenStateReducer : StateReducer<FacultyPickerScreenState, FacultyPickerScreenStateUpdate> {
 override val initial: FacultyPickerScreenState
  get() = FacultyPickerScreenState()
}