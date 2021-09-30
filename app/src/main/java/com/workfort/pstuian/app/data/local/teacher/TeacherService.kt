package com.workfort.pstuian.app.data.local.teacher

import androidx.lifecycle.LiveData

class TeacherService(private val teacherDao: TeacherDao) {
    fun getAllLive(): LiveData<List<TeacherEntity>> {
        return teacherDao.getAllLive()
    }

    fun getAllLive(faculty: String): LiveData<List<TeacherEntity>> {
        return teacherDao.getAllLive(faculty)
    }

    fun getAll(): List<TeacherEntity> {
        return teacherDao.getAll()
    }

    fun getAll(faculty: String): List<TeacherEntity> {
        return teacherDao.getAll(faculty)
    }

    fun get(id: String) : TeacherEntity{
        return teacherDao.get(id)
    }

    fun insert(teacherEntity: TeacherEntity) {
        teacherDao.insert(teacherEntity)
    }

    fun insertAll(teacherEntities: List<TeacherEntity>) {
        teacherDao.insertAll(teacherEntities)
    }

    fun update(teacherEntity: TeacherEntity) {
        teacherDao.update(teacherEntity)
    }

    fun delete(teacherEntity: TeacherEntity) {
        teacherDao.delete(teacherEntity)
    }

    fun deleteAll(faculty: String) {
        teacherDao.deleteAll(faculty)
    }

    fun deleteAll() {
        teacherDao.deleteAll()
    }
}