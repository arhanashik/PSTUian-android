package com.workfort.pstuian.app.data.local.teacher

import androidx.lifecycle.LiveData

class TeacherService(private val teacherDao: TeacherDao) {
    fun getAllLive(): LiveData<List<TeacherEntity>> = teacherDao.getAllLive()
    fun getAllLive(faculty: String): LiveData<List<TeacherEntity>> =
        teacherDao.getAllLive(faculty)
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