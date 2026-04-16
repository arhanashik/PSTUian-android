package com.workfort.pstuian.data.local.database.service

import com.workfort.pstuian.data.local.database.dao.StudentDao
import com.workfort.pstuian.data.local.database.entity.toDb
import com.workfort.pstuian.data.local.database.entity.toDomain
import com.workfort.pstuian.featuredomain.model.StudentEntity

class StudentDbService(private val studentDao: StudentDao) {
    suspend fun getAll(): List<StudentEntity> = studentDao.getAll().map { it.toDomain() }
    suspend fun getAll(facultyId: Int, batchId: Int): List<StudentEntity> =
        studentDao.getAll(facultyId, batchId).map { it.toDomain() }
    suspend fun get(id: Int) : StudentEntity? = studentDao.get(id)?.toDomain()
    suspend fun insert(studentEntity: StudentEntity) = studentDao.insert(studentEntity.toDb())
    suspend fun insertAll(entities: List<StudentEntity>) = studentDao.insertAll(entities.map { it.toDb() })
    suspend fun update(studentEntity: StudentEntity) = studentDao.update(studentEntity.toDb())
    suspend fun delete(studentEntity: StudentEntity) = studentDao.delete(studentEntity.toDb())
    suspend fun deleteAll(faculty: Int) = studentDao.deleteAll(faculty)
    suspend fun deleteAll(faculty: Int, batch: Int) = studentDao.deleteAll(faculty, batch)
    suspend fun deleteAll() = studentDao.deleteAll()
}
