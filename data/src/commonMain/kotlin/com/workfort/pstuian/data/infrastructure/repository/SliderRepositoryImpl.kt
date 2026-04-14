package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.local.database.service.SliderDbService
import com.workfort.pstuian.data.remote.domain.SliderApiHelper
import com.workfort.pstuian.featuredomain.model.SliderEntity
import com.workfort.pstuian.featuredomain.repository.SliderRepository

class SliderRepositoryImpl(
    private val dbService: SliderDbService,
    private val helper: SliderApiHelper,
) : SliderRepository {
    override suspend fun getSliders(forceRefresh: Boolean): List<SliderEntity> {
        val existingData = if (forceRefresh) emptyList() else dbService.getAll()
        if (existingData.isEmpty()) {
            val newData = helper.getAll().map { it.toEntity() }
            dbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    override suspend fun deleteAll() {
        dbService.deleteAll()
    }
}