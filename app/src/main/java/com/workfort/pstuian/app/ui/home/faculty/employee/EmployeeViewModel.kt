package com.workfort.pstuian.app.ui.home.faculty.employee

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.workfort.pstuian.app.data.local.database.DatabaseHelper
import com.workfort.pstuian.app.data.local.employee.EmployeeEntity
import com.workfort.pstuian.app.data.local.employee.EmployeeService

class EmployeeViewModel : ViewModel() {
    private var employeeService: EmployeeService = DatabaseHelper.provideEmployeeService()
    private lateinit var employeesLiveData: LiveData<List<EmployeeEntity>>

    fun getEmployees(faculty: String): LiveData<List<EmployeeEntity>> {
        employeesLiveData = employeeService.getAllLive(faculty)

        return employeesLiveData
    }

    fun insertEmployees(employees: ArrayList<EmployeeEntity>) {
        Thread { employeeService.insertAll(employees) }.start()
    }
}
