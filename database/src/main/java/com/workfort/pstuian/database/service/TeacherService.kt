package com.workfort.pstuian.database.service

import com.workfort.pstuian.database.dao.TeacherDao
import com.workfort.pstuian.model.TeacherEntity

class TeacherService(private val teacherDao: TeacherDao) {
    suspend fun getAll(): List<TeacherEntity> = teacherDao.getAll()
    suspend fun getAll(facultyId: Int): List<TeacherEntity> = teacherDao.getAll(facultyId)
    suspend fun get(id: Int) : TeacherEntity? = teacherDao.get(id)
    suspend fun insert(teacherEntity: TeacherEntity) = teacherDao.insert(teacherEntity)
    suspend fun insertAll(entities: List<TeacherEntity>) = teacherDao.insertAll(entities)
    suspend fun update(teacherEntity: TeacherEntity) = teacherDao.update(teacherEntity)
    suspend fun delete(teacherEntity: TeacherEntity) = teacherDao.delete(teacherEntity)
    suspend fun deleteAll(faculty: String) = teacherDao.deleteAll(faculty)
    suspend fun deleteAll() = teacherDao.deleteAll()
}