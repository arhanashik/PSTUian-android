package com.workfort.pstuian.data.local.database.service

import com.workfort.pstuian.data.local.database.dao.EmployeeDao
import com.workfort.pstuian.data.local.database.entity.toDb
import com.workfort.pstuian.data.local.database.entity.toDomain
import com.workfort.pstuian.featuredomain.model.EmployeeEntity

class EmployeeDbService(private val dao: EmployeeDao) {
    suspend fun getAll(): List<EmployeeEntity> = dao.getAll().map { it.toDomain() }
    suspend fun getAll(facultyId: Int): List<EmployeeEntity> = dao.getAll(facultyId).map { it.toDomain() }
    suspend fun get(id: Int) : EmployeeEntity = dao.get(id).toDomain()
    suspend fun insert(entity: EmployeeEntity) = dao.insert(entity.toDb())
    suspend fun insertAll(entities: List<EmployeeEntity>) = dao.insertAll(entities.map { it.toDb() })
    suspend fun update(entity: EmployeeEntity) = dao.update(entity.toDb())
    suspend fun delete(entity: EmployeeEntity) = dao.delete(entity.toDb())
    suspend fun deleteAll(faculty: Int) = dao.deleteAll(faculty)
    suspend fun deleteAll() = dao.deleteAll()
}
