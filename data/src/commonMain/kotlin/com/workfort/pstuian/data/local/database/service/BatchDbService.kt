package com.workfort.pstuian.data.local.database.service

import com.workfort.pstuian.data.local.database.dao.BatchDao
import com.workfort.pstuian.featuredomain.model.BatchEntity

class BatchDbService(private val batchDao: BatchDao) {
    suspend fun getAll(): List<BatchEntity> = batchDao.getAll()
    suspend fun getAll(facultyId: Int): List<BatchEntity> = batchDao.getAll(facultyId)
    suspend fun get(id: Int) : BatchEntity? = batchDao.get(id)
    suspend fun insert(batchEntity: BatchEntity) = batchDao.insert(batchEntity)
    suspend fun insertAll(entities: List<BatchEntity>) = batchDao.insertAll(entities)
    suspend fun update(batchEntity: BatchEntity) = batchDao.update(batchEntity)
    suspend fun delete(batchEntity: BatchEntity) = batchDao.delete(batchEntity)
    suspend fun deleteAll(faculty: Int) = batchDao.deleteAll(faculty)
    suspend fun deleteAll() = batchDao.deleteAll()
}