package com.workfort.pstuian.app.data.local.teacher

import androidx.lifecycle.LiveData

class TeacherService(private val teacherDao: TeacherDao) {
    fun getAllLive(): LiveData<List<TeacherEntity>> = teacherDao.getAllLive()
    fun getAllLive(faculty: String): LiveData<List<TeacherEntity>> =
        teacherDao.getAllLive(faculty)
    suspend fun getAll(): List<TeacherEntity> = teacherDao.getAll()
    suspend fun getAll(facultyId: Int): List<TeacherEntity> = teacherDao.getAll(facultyId)
    fun get(id: String) : TeacherEntity = teacherDao.get(id)
    fun insert(teacherEntity: TeacherEntity) = teacherDao.insert(teacherEntity)
    suspend fun insertAll(entities: List<TeacherEntity>) = teacherDao.insertAll(entities)
    fun update(teacherEntity: TeacherEntity) = teacherDao.update(teacherEntity)
    fun delete(teacherEntity: TeacherEntity) = teacherDao.delete(teacherEntity)
    suspend fun deleteAll(faculty: String) = teacherDao.deleteAll(faculty)
    suspend fun deleteAll() = teacherDao.deleteAll()
}