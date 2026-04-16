package com.workfort.pstuian.data.local.database.service

import com.workfort.pstuian.data.local.database.dao.FacultyDao
import com.workfort.pstuian.data.local.database.entity.toDb
import com.workfort.pstuian.data.local.database.entity.toDomain
import com.workfort.pstuian.featuredomain.model.FacultyEntity

class FacultyDbService(private val facultyDao: FacultyDao) {
    suspend fun getAll(): List<FacultyEntity> = facultyDao.getAll().map { it.toDomain() }
    suspend fun get(id: Int) : FacultyEntity? = facultyDao.get(id)?.toDomain()
    suspend fun insert(facultyEntity: FacultyEntity) = facultyDao.insert(facultyEntity.toDb())
    suspend fun insertAll(facultyEntities: List<FacultyEntity>) = facultyDao.insertAll(facultyEntities.map { it.toDb() })
    suspend fun update(facultyEntity: FacultyEntity) = facultyDao.update(facultyEntity.toDb())
    suspend fun delete(facultyEntity: FacultyEntity) = facultyDao.delete(facultyEntity.toDb())
    suspend fun deleteAll() = facultyDao.deleteAll()
}
