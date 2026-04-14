package com.workfort.pstuian.data.local.database.service

import com.workfort.pstuian.data.local.database.dao.FacultyDao
import com.workfort.pstuian.featuredomain.model.FacultyEntity

class FacultyDbService(private val facultyDao: FacultyDao) {
    suspend fun getAll(): List<FacultyEntity> = facultyDao.getAll()
    suspend fun get(id: Int) : FacultyEntity? = facultyDao.get(id)
    suspend fun insert(facultyEntity: FacultyEntity) = facultyDao.insert(facultyEntity)
    suspend fun insertAll(facultyEntities: List<FacultyEntity>) = facultyDao.insertAll(facultyEntities)
    suspend fun update(facultyEntity: FacultyEntity) = facultyDao.update(facultyEntity)
    suspend fun delete(facultyEntity: FacultyEntity) = facultyDao.delete(facultyEntity)
    suspend fun deleteAll() = facultyDao.deleteAll()
}