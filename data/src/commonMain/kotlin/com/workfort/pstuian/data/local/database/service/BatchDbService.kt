package com.workfort.pstuian.data.local.database.service

import com.workfort.pstuian.data.local.database.dao.BatchDao
import com.workfort.pstuian.data.local.database.entity.toDb
import com.workfort.pstuian.data.local.database.entity.toDomain
import com.workfort.pstuian.featuredomain.model.BatchEntity

class BatchDbService(private val batchDao: BatchDao) {
    suspend fun getAll(): List<BatchEntity> = batchDao.getAll().map { it.toDomain() }
    suspend fun getAll(facultyId: Int): List<BatchEntity> = batchDao.getAll(facultyId).map { it.toDomain() }
    suspend fun get(id: Int) : BatchEntity? = batchDao.get(id)?.toDomain()
    suspend fun insert(batchEntity: BatchEntity) = batchDao.insert(batchEntity.toDb())
    suspend fun insertAll(entities: List<BatchEntity>) = batchDao.insertAll(entities.map { it.toDb() })
    suspend fun update(batchEntity: BatchEntity) = batchDao.update(batchEntity.toDb())
    suspend fun delete(batchEntity: BatchEntity) = batchDao.delete(batchEntity.toDb())
    suspend fun deleteAll(faculty: Int) = batchDao.deleteAll(faculty)
    suspend fun deleteAll() = batchDao.deleteAll()
}
