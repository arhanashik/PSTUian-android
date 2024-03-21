package com.workfort.pstuian.database.service

import com.workfort.pstuian.database.dao.BatchDao
import com.workfort.pstuian.model.BatchEntity

class BatchService(private val batchDao: BatchDao) {
    suspend fun getAll(): List<BatchEntity> = batchDao.getAll()
    suspend fun getAll(facultyId: Int): List<BatchEntity> = batchDao.getAll(facultyId)
    suspend fun get(id: Int) : BatchEntity? = batchDao.get(id)
    suspend fun insert(batchEntity: BatchEntity) = batchDao.insert(batchEntity)
    suspend fun insertAll(entities: List<BatchEntity>) = batchDao.insertAll(entities)
    fun update(batchEntity: BatchEntity) = batchDao.update(batchEntity)
    fun delete(batchEntity: BatchEntity) = batchDao.delete(batchEntity)
    suspend fun deleteAll(faculty: String) = batchDao.deleteAll(faculty)
    suspend fun deleteAll() = batchDao.deleteAll()
}