package com.workfort.pstuian.app.data.local.batch

import androidx.lifecycle.LiveData

class BatchService(private val batchDao: BatchDao) {
    fun getAllLive(): LiveData<List<BatchEntity>> {
        return batchDao.getAllLive()
    }

    fun getAllLive(faculty: String): LiveData<List<BatchEntity>> {
        return batchDao.getAllLive(faculty)
    }

    fun getAll(): List<BatchEntity> {
        return batchDao.getAll()
    }

    fun getAll(faculty: String): List<BatchEntity> {
        return batchDao.getAll(faculty)
    }

    fun get(session: String) : BatchEntity{
        return batchDao.get(session)
    }

    fun insert(batchEntity: BatchEntity) {
        batchDao.insert(batchEntity)
    }

    fun insertAll(batchEntities: List<BatchEntity>) {
        batchDao.insertAll(batchEntities)
    }

    fun update(batchEntity: BatchEntity) {
        batchDao.update(batchEntity)
    }

    fun delete(batchEntity: BatchEntity) {
        batchDao.delete(batchEntity)
    }

    fun deleteAll(faculty: String) {
        batchDao.deleteAll(faculty)
    }

    fun deleteAll() {
        batchDao.deleteAll()
    }
}