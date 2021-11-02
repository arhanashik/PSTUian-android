package com.workfort.pstuian.app.data.local.employee

import androidx.lifecycle.LiveData

class EmployeeService(private val dao: EmployeeDao) {
    fun getAllLive(): LiveData<List<EmployeeEntity>> = dao.getAllLive()
    fun getAllLive(facultyId: Int): LiveData<List<EmployeeEntity>> = dao.getAllLive(facultyId)
    fun getAll(): List<EmployeeEntity> = dao.getAll()
    suspend fun getAll(facultyId: Int): List<EmployeeEntity> = dao.getAll(facultyId)
    fun get(id: String) : EmployeeEntity = dao.get(id)
    fun insert(entity: EmployeeEntity) = dao.insert(entity)
    suspend fun insertAll(entities: List<EmployeeEntity>) = dao.insertAll(entities)
    fun update(entity: EmployeeEntity) = dao.update(entity)
    fun delete(entity: EmployeeEntity) = dao.delete(entity)
    suspend fun deleteAll(faculty: String) = dao.deleteAll(faculty)
    suspend fun deleteAll() = dao.deleteAll()
}