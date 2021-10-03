package com.workfort.pstuian.app.data.local.faculty

import androidx.lifecycle.LiveData

class FacultyService(private val facultyDao: FacultyDao) {
    fun getAllLive(): LiveData<List<FacultyEntity>> = facultyDao.getAllLive()
    suspend fun getAll(): List<FacultyEntity> = facultyDao.getAll()
    fun get(shortTitle: String) : FacultyEntity = facultyDao.get(shortTitle)
    fun insert(facultyEntity: FacultyEntity) = facultyDao.insert(facultyEntity)
    suspend fun insertAll(facultyEntities: List<FacultyEntity>) = facultyDao.insertAll(facultyEntities)
    fun update(facultyEntity: FacultyEntity) = facultyDao.update(facultyEntity)
    fun delete(facultyEntity: FacultyEntity) = facultyDao.delete(facultyEntity)
    suspend fun deleteAll() = facultyDao.deleteAll()
}