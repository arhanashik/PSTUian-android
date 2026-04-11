package com.workfort.pstuian.database.service

import com.workfort.pstuian.database.dao.EmployeeDao
import com.workfort.pstuian.model.EmployeeEntity

class EmployeeDbService(private val dao: EmployeeDao) {
    suspend fun getAll(): List<EmployeeEntity> = dao.getAll()
    suspend fun getAll(facultyId: Int): List<EmployeeEntity> = dao.getAll(facultyId)
    suspend fun get(id: Int) : EmployeeEntity = dao.get(id)
    suspend fun insert(entity: EmployeeEntity) = dao.insert(entity)
    suspend fun insertAll(entities: List<EmployeeEntity>) = dao.insertAll(entities)
    suspend fun update(entity: EmployeeEntity) = dao.update(entity)
    suspend fun delete(entity: EmployeeEntity) = dao.delete(entity)
    suspend fun deleteAll(faculty: Int) = dao.deleteAll(faculty)
    suspend fun deleteAll() = dao.deleteAll()
}