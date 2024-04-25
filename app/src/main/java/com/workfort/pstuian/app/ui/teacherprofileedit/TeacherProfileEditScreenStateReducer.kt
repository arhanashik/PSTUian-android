package com.workfort.pstuian.app.ui.teacherprofileedit

import com.workfort.pstuian.view.service.StateReducer


class TeacherProfileEditScreenStateReducer : StateReducer<TeacherProfileEditScreenState, TeacherProfileEditScreenStateUpdate> {
 override val initial: TeacherProfileEditScreenState
  get() = TeacherProfileEditScreenState()
}