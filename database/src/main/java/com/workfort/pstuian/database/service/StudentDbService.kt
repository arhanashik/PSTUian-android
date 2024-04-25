package com.workfort.pstuian.database.service

import com.workfort.pstuian.database.dao.StudentDao
import com.workfort.pstuian.model.StudentEntity

class StudentDbService(private val studentDao: StudentDao) {
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