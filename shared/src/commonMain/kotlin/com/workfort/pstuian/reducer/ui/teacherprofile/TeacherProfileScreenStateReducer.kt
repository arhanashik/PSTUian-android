package com.workfort.pstuian.reducer.ui.teacherprofile

import com.workfort.pstuian.reducer.service.StateReducer


class TeacherProfileScreenStateReducer : StateReducer<TeacherProfileScreenState, TeacherProfileScreenStateUpdate> {
    override val initial: TeacherProfileScreenState
        get() = TeacherProfileScreenState()
}