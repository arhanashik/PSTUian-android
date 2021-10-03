package com.workfort.pstuian.app.data.local.course

import androidx.lifecycle.LiveData

class CourseService(private val dao: CourseDao) {
    fun getAllLive(): LiveData<List<CourseEntity>> = dao.getAllLive()
    fun getAllLive(faculty: String): LiveData<List<CourseEntity>> =
        dao.getAllLive(faculty)
    fun getAll(): List<CourseEntity> = dao.getAll()
    suspend fun getAll(facultyId: Int): List<CourseEntity> = dao.getAll(facultyId)
    fun get(id: String) : CourseEntity = dao.get(id)
    fun insert(courseEntity: CourseEntity) = dao.insert(courseEntity)
    suspend fun insertAll(entities: List<CourseEntity>) = dao.insertAll(entities)
    fun update(entity: CourseEntity) = dao.update(entity)
    fun delete(entity: CourseEntity) = dao.delete(entity)
    suspend fun deleteAll(faculty: String) = dao.deleteAll(faculty)
    suspend fun deleteAll() = dao.deleteAll()
}