package com.workfort.pstuian.database.service

import com.workfort.pstuian.database.dao.CourseDao
import com.workfort.pstuian.model.CourseEntity

class CourseDbService(private val dao: CourseDao) {
    suspend fun getAll(): List<CourseEntity> = dao.getAll()
    suspend fun getAll(facultyId: Int): List<CourseEntity> = dao.getAll(facultyId)
    suspend fun get(id: Int) : CourseEntity = dao.get(id)
    suspend fun insert(courseEntity: CourseEntity) = dao.insert(courseEntity)
    suspend fun insertAll(entities: List<CourseEntity>) = dao.insertAll(entities)
    suspend fun update(entity: CourseEntity) = dao.update(entity)
    suspend fun delete(entity: CourseEntity) = dao.delete(entity)
    suspend fun deleteAll(faculty: Int) = dao.deleteAll(faculty)
    suspend fun deleteAll() = dao.deleteAll()
}