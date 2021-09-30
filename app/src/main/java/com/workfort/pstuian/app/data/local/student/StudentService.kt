package com.workfort.pstuian.app.data.local.student

import androidx.lifecycle.LiveData

class StudentService(private val studentDao: StudentDao) {
    fun getAllLive(): LiveData<List<StudentEntity>> {
        return studentDao.getAllLive()
    }

    fun getAllLive(faculty: String, batch: String): LiveData<List<StudentEntity>> {
        return studentDao.getAllLive(faculty, batch)
    }

    fun getAll(): List<StudentEntity> {
        return studentDao.getAll()
    }

    fun getAll(faculty: String, batch: String): List<StudentEntity> {
        return studentDao.getAll(faculty, batch)
    }

    fun get(id: String) : StudentEntity{
        return studentDao.get(id)
    }

    fun insert(studentEntity: StudentEntity) {
        studentDao.insert(studentEntity)
    }

    fun insertAll(studentEntities: List<StudentEntity>) {
        studentDao.insertAll(studentEntities)
    }

    fun update(studentEntity: StudentEntity) {
        studentDao.update(studentEntity)
    }

    fun delete(studentEntity: StudentEntity) {
        studentDao.delete(studentEntity)
    }

    fun deleteAll(faculty: String) {
        studentDao.deleteAll(faculty)
    }

    fun deleteAll(faculty: String, batch: String) {
        studentDao.deleteAll(faculty, batch)
    }

    fun deleteAll() {
        studentDao.deleteAll()
    }
}