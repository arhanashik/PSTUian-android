package com.workfort.pstuian.data.local.database.service

import com.workfort.pstuian.data.local.database.dao.ConfigDao
import com.workfort.pstuian.data.local.database.entity.toDb
import com.workfort.pstuian.data.local.database.entity.toDomain
import com.workfort.pstuian.featuredomain.model.ConfigEntity

class ConfigDbService(private val dao: ConfigDao) {
    suspend fun getLatest(): ConfigEntity? = dao.getLatest()?.toDomain()
    suspend fun get(id: Int) : ConfigEntity = dao.get(id).toDomain()
    suspend fun insert(entity: ConfigEntity) = dao.insert(entity.toDb())
    suspend fun update(entity: ConfigEntity) = dao.update(entity.toDb())
    suspend fun delete(entity: ConfigEntity) = dao.delete(entity.toDb())
    suspend fun deleteAll() = dao.deleteAll()
}
