package com.workfort.pstuian.app.ui.faculty

import com.workfort.pstuian.view.service.StateReducer


class FacultyScreenStateReducer : StateReducer<FacultyScreenState, FacultyScreenStateUpdate> {
 override val initial: FacultyScreenState
  get() = FacultyScreenState()
}