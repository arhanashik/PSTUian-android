package com.workfort.pstuian.app.data.local.batch

import androidx.lifecycle.LiveData

class BatchService(private val batchDao: BatchDao) {
    fun getAllLive(): LiveData<List<BatchEntity>> = batchDao.getAllLive()
    fun getAllLive(facultyId: Int): LiveData<List<BatchEntity>> = batchDao.getAllLive(facultyId)
    suspend fun getAll(): List<BatchEntity> = batchDao.getAll()
    suspend fun getAll(facultyId: Int): List<BatchEntity> = batchDao.getAll(facultyId)
    fun get(session: String) : BatchEntity = batchDao.get(session)
    fun insert(batchEntity: BatchEntity) = batchDao.insert(batchEntity)
    suspend fun insertAll(entities: List<BatchEntity>) = batchDao.insertAll(entities)
    fun update(batchEntity: BatchEntity) = batchDao.update(batchEntity)
    fun delete(batchEntity: BatchEntity) = batchDao.delete(batchEntity)
    suspend fun deleteAll(faculty: String) = batchDao.deleteAll(faculty)
    suspend fun deleteAll() = batchDao.deleteAll()
}