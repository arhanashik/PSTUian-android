package com.workfort.pstuian.app.ui.teacherprofile

import com.workfort.pstuian.view.service.StateReducer


class TeacherProfileScreenStateReducer : StateReducer<TeacherProfileScreenState, TeacherProfileScreenStateUpdate> {
 override val initial: TeacherProfileScreenState
  get() = TeacherProfileScreenState()
}