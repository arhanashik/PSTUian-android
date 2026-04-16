package com.workfort.pstuian.data.local.database.service

import com.workfort.pstuian.data.local.database.dao.TeacherDao
import com.workfort.pstuian.data.local.database.entity.toDb
import com.workfort.pstuian.data.local.database.entity.toDomain
import com.workfort.pstuian.featuredomain.model.TeacherEntity

class TeacherDbService(private val teacherDao: TeacherDao) {
    suspend fun getAll(): List<TeacherEntity> = teacherDao.getAll().map { it.toDomain() }
    suspend fun getAll(facultyId: Int): List<TeacherEntity> = teacherDao.getAll(facultyId).map { it.toDomain() }
    suspend fun get(id: Int) : TeacherEntity? = teacherDao.get(id)?.toDomain()
    suspend fun insert(teacherEntity: TeacherEntity) = teacherDao.insert(teacherEntity.toDb())
    suspend fun insertAll(entities: List<TeacherEntity>) = teacherDao.insertAll(entities.map { it.toDb() })
    suspend fun update(teacherEntity: TeacherEntity) = teacherDao.update(teacherEntity.toDb())
    suspend fun delete(teacherEntity: TeacherEntity) = teacherDao.delete(teacherEntity.toDb())
    suspend fun deleteAll(faculty: Int) = teacherDao.deleteAll(faculty)
    suspend fun deleteAll() = teacherDao.deleteAll()
}
