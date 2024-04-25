package com.workfort.pstuian.app.ui.employeeprofile

import com.workfort.pstuian.view.service.StateReducer


class EmployeeProfileScreenStateReducer : StateReducer<EmployeeProfileScreenState, EmployeeProfileScreenStateUpdate> {
 override val initial: EmployeeProfileScreenState
  get() = EmployeeProfileScreenState()
}