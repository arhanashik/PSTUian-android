package com.workfort.pstuian.app.data.local.student

import androidx.lifecycle.LiveData

class StudentService(private val studentDao: StudentDao) {
    fun getAllLive(): LiveData<List<StudentEntity>> = studentDao.getAllLive()
    fun getAllLive(faculty: String, batch: String): LiveData<List<StudentEntity>> =
        studentDao.getAllLive(faculty, batch)
    suspend fun getAll(): List<StudentEntity> = studentDao.getAll()
    suspend fun getAll(facultyId: Int, batchId: Int): List<StudentEntity> =
        studentDao.getAll(facultyId, batchId)
    suspend fun get(id: Int) : StudentEntity? = studentDao.get(id)
    suspend fun insert(studentEntity: StudentEntity) = studentDao.insert(studentEntity)
    suspend fun insertAll(entities: List<StudentEntity>) = studentDao.insertAll(entities)
    suspend fun update(studentEntity: StudentEntity) = studentDao.update(studentEntity)
    suspend fun delete(studentEntity: StudentEntity) = studentDao.delete(studentEntity)
    fun deleteAll(faculty: String) = studentDao.deleteAll(faculty)
    suspend fun deleteAll(faculty: String, batch: String) = studentDao.deleteAll(faculty, batch)
    suspend fun deleteAll() = studentDao.deleteAll()
}