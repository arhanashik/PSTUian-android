package com.workfort.pstuian.app.data.remote

import com.workfort.pstuian.app.data.local.employee.EmployeeEntity

data class EmployeeResponse (val success: Boolean,
                             val message: String,
                             val employees: ArrayList<EmployeeEntity>)