package com.workfort.pstuian.reducer.ui.studentprofile

import com.workfort.pstuian.reducer.service.StateReducer


class StudentProfileScreenStateReducer : StateReducer<StudentProfileScreenState, StudentProfileScreenStateUpdate> {
    override val initial: StudentProfileScreenState
        get() = StudentProfileScreenState()
}