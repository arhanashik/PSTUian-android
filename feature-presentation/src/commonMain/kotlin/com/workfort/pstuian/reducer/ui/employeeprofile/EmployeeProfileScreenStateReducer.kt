package com.workfort.pstuian.reducer.ui.employeeprofile

import com.workfort.pstuian.reducer.service.StateReducer

class EmployeeProfileScreenStateReducer : StateReducer<EmployeeProfileScreenState, EmployeeProfileScreenStateUpdate> {
    override val initial: EmployeeProfileScreenState
        get() = EmployeeProfileScreenState()
}
