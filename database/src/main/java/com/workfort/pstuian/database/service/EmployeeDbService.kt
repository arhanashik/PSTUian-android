package com.workfort.pstuian.database.service

import com.workfort.pstuian.database.dao.EmployeeDao
import com.workfort.pstuian.model.EmployeeEntity

class EmployeeDbService(private val dao: EmployeeDao) {
    fun getAll(): List<EmployeeEntity> = dao.getAll()
    suspend fun getAll(facultyId: Int): List<EmployeeEntity> = dao.getAll(facultyId)
    fun get(id: Int) : EmployeeEntity = dao.get(id)
    fun insert(entity: EmployeeEntity) = dao.insert(entity)
    suspend fun insertAll(entities: List<EmployeeEntity>) = dao.insertAll(entities)
    fun update(entity: EmployeeEntity) = dao.update(entity)
    fun delete(entity: EmployeeEntity) = dao.delete(entity)
    suspend fun deleteAll(faculty: String) = dao.deleteAll(faculty)
    suspend fun deleteAll() = dao.deleteAll()
}