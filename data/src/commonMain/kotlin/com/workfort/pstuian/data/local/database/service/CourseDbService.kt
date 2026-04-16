package com.workfort.pstuian.data.local.database.service

import com.workfort.pstuian.data.local.database.dao.CourseDao
import com.workfort.pstuian.data.local.database.entity.toDb
import com.workfort.pstuian.data.local.database.entity.toDomain
import com.workfort.pstuian.featuredomain.model.CourseEntity

class CourseDbService(private val dao: CourseDao) {
    suspend fun getAll(): List<CourseEntity> = dao.getAll().map { it.toDomain() }
    suspend fun getAll(facultyId: Int): List<CourseEntity> = dao.getAll(facultyId).map { it.toDomain() }
    suspend fun get(id: Int) : CourseEntity = dao.get(id).toDomain()
    suspend fun insert(courseEntity: CourseEntity) = dao.insert(courseEntity.toDb())
    suspend fun insertAll(entities: List<CourseEntity>) = dao.insertAll(entities.map { it.toDb() })
    suspend fun update(entity: CourseEntity) = dao.update(entity.toDb())
    suspend fun delete(entity: CourseEntity) = dao.delete(entity.toDb())
    suspend fun deleteAll(faculty: Int) = dao.deleteAll(faculty)
    suspend fun deleteAll() = dao.deleteAll()
}
