package com.workfort.pstuian.app.ui.faculty.listener

import com.workfort.pstuian.app.data.local.employee.EmployeeEntity

interface EmployeeClickEvent{
    fun onClickEmployee(employee: EmployeeEntity)
}