package com.workfort.pstuian.app.ui.studentprofileedit

import com.workfort.pstuian.view.service.StateReducer


class StudentProfileEditScreenStateReducer : StateReducer<StudentProfileEditScreenState, StudentProfileEditScreenStateUpdate> {
 override val initial: StudentProfileEditScreenState
  get() = StudentProfileEditScreenState()
}