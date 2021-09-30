package com.workfort.pstuian.app.data.local.courseschedule

import androidx.lifecycle.LiveData

class CourseScheduleService(private val courseScheduleDao: CourseScheduleDao) {
    fun getAllLive(): LiveData<List<CourseScheduleEntity>> {
        return courseScheduleDao.getAllLive()
    }

    fun getAllLive(faculty: String): LiveData<List<CourseScheduleEntity>> {
        return courseScheduleDao.getAllLive(faculty)
    }

    fun getAll(): List<CourseScheduleEntity> {
        return courseScheduleDao.getAll()
    }

    fun getAll(faculty: String): List<CourseScheduleEntity> {
        return courseScheduleDao.getAll(faculty)
    }

    fun get(id: String) : CourseScheduleEntity{
        return courseScheduleDao.get(id)
    }

    fun insert(courseScheduleEntity: CourseScheduleEntity) {
        courseScheduleDao.insert(courseScheduleEntity)
    }

    fun insertAll(courseScheduleEntities: List<CourseScheduleEntity>) {
        courseScheduleDao.insertAll(courseScheduleEntities)
    }

    fun update(courseScheduleEntity: CourseScheduleEntity) {
        courseScheduleDao.update(courseScheduleEntity)
    }

    fun delete(courseScheduleEntity: CourseScheduleEntity) {
        courseScheduleDao.delete(courseScheduleEntity)
    }

    fun deleteAll(faculty: String) {
        courseScheduleDao.deleteAll(faculty)
    }

    fun deleteAll() {
        courseScheduleDao.deleteAll()
    }
}