package com.workfort.pstuian.reducer.ui.teacherprofileedit

import com.workfort.pstuian.reducer.service.StateReducer


class TeacherProfileEditScreenStateReducer : StateReducer<TeacherProfileEditScreenState, TeacherProfileEditScreenStateUpdate> {
    override val initial: TeacherProfileEditScreenState
        get() = TeacherProfileEditScreenState()
}