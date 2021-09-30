package com.workfort.pstuian.app.data.local.employee

import androidx.lifecycle.LiveData

class EmployeeService(private val employeeDao: EmployeeDao) {
    fun getAllLive(): LiveData<List<EmployeeEntity>> {
        return employeeDao.getAllLive()
    }

    fun getAllLive(faculty: String): LiveData<List<EmployeeEntity>> {
        return employeeDao.getAllLive(faculty)
    }

    fun getAll(): List<EmployeeEntity> {
        return employeeDao.getAll()
    }

    fun getAll(faculty: String): List<EmployeeEntity> {
        return employeeDao.getAll(faculty)
    }

    fun get(id: String) : EmployeeEntity{
        return employeeDao.get(id)
    }

    fun insert(employeeEntity: EmployeeEntity) {
        employeeDao.insert(employeeEntity)
    }

    fun insertAll(employeeEntities: List<EmployeeEntity>) {
        employeeDao.insertAll(employeeEntities)
    }

    fun update(employeeEntity: EmployeeEntity) {
        employeeDao.update(employeeEntity)
    }

    fun delete(employeeEntity: EmployeeEntity) {
        employeeDao.delete(employeeEntity)
    }

    fun deleteAll(faculty: String) {
        employeeDao.deleteAll(faculty)
    }

    fun deleteAll() {
        employeeDao.deleteAll()
    }
}