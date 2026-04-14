package com.workfort.pstuian.data.local.database.service

import com.workfort.pstuian.data.local.database.dao.ConfigDao
import com.workfort.pstuian.featuredomain.model.ConfigEntity

class ConfigDbService(private val dao: ConfigDao) {
    suspend fun getLatest(): ConfigEntity? = dao.getLatest()
    suspend fun get(id: Int) : ConfigEntity = dao.get(id)
    suspend fun insert(entity: ConfigEntity) = dao.insert(entity)
    suspend fun update(entity: ConfigEntity) = dao.update(entity)
    suspend fun delete(entity: ConfigEntity) = dao.delete(entity)
    suspend fun deleteAll() = dao.deleteAll()
}