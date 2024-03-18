package com.workfort.pstuian.app.ui.faculty.listener

import com.workfort.pstuian.model.EmployeeEntity

interface EmployeeClickEvent{
    fun onClickEmployee(employee: EmployeeEntity)
}