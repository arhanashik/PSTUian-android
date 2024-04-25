package com.workfort.pstuian.database.service

import com.workfort.pstuian.database.dao.FacultyDao
import com.workfort.pstuian.model.FacultyEntity

class FacultyDbService(private val facultyDao: FacultyDao) {
    suspend fun getAll(): List<FacultyEntity> = facultyDao.getAll()
    suspend fun get(id: Int) : FacultyEntity? = facultyDao.get(id)
    fun insert(facultyEntity: FacultyEntity) = facultyDao.insert(facultyEntity)
    suspend fun insertAll(facultyEntities: List<FacultyEntity>) = facultyDao.insertAll(facultyEntities)
    fun update(facultyEntity: FacultyEntity) = facultyDao.update(facultyEntity)
    fun delete(facultyEntity: FacultyEntity) = facultyDao.delete(facultyEntity)
    suspend fun deleteAll() = facultyDao.deleteAll()
}