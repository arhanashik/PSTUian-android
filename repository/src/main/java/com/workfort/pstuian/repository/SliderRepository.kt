package com.workfort.pstuian.repository

import com.workfort.pstuian.database.service.SliderDbService
import com.workfort.pstuian.model.SliderEntity
import com.workfort.pstuian.networking.domain.SliderApiHelper

class SliderRepository(
    private val dbService: SliderDbService,
    private val helper: SliderApiHelper,
) {
    suspend fun getSliders(forceRefresh: Boolean = false): List<SliderEntity> {
        val existingData = if (forceRefresh) emptyList() else dbService.getAll()
        if (existingData.isEmpty()) {
            val newData = helper.getAll()
            dbService.insertAll(newData)
            return newData
        }

        return existingData
    }

    suspend fun deleteAll() {
        dbService.deleteAll()
    }
}