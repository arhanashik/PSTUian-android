package com.workfort.pstuian.app.ui.students

import com.workfort.pstuian.view.service.StateReducer


class StudentsScreenStateReducer : StateReducer<StudentsScreenState, StudentsScreenStateUpdate> {
 override val initial: StudentsScreenState
  get() = StudentsScreenState()
}