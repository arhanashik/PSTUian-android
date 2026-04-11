package com.workfort.pstuian.reducer.ui.students

import com.workfort.pstuian.reducer.service.StateReducer


class StudentsScreenStateReducer : StateReducer<StudentsScreenState, StudentsScreenStateUpdate> {
    override val initial: StudentsScreenState
        get() = StudentsScreenState()
}