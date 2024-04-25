package com.workfort.pstuian.app.ui.studentprofile

import com.workfort.pstuian.view.service.StateReducer


class StudentProfileScreenStateReducer : StateReducer<StudentProfileScreenState, StudentProfileScreenStateUpdate> {
 override val initial: StudentProfileScreenState
  get() = StudentProfileScreenState()
}