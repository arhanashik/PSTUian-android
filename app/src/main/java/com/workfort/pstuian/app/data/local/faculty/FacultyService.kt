package com.workfort.pstuian.app.data.local.faculty

import androidx.lifecycle.LiveData

class FacultyService(private val facultyDao: FacultyDao) {
    fun getAllLive(): LiveData<List<FacultyEntity>> {
        return facultyDao.getAllLive()
    }

    fun getAll(): List<FacultyEntity> {
        return facultyDao.getAll()
    }

    fun get(shortTitle: String) : FacultyEntity{
        return facultyDao.get(shortTitle)
    }

    fun insert(facultyEntity: FacultyEntity) {
        facultyDao.insert(facultyEntity)
    }

    fun insertAll(facultyEntities: List<FacultyEntity>) {
        facultyDao.insertAll(facultyEntities)
    }

    fun update(facultyEntity: FacultyEntity) {
        facultyDao.update(facultyEntity)
    }

    fun delete(facultyEntity: FacultyEntity) {
        facultyDao.delete(facultyEntity)
    }

    fun deleteAll() {
        facultyDao.deleteAll()
    }
}