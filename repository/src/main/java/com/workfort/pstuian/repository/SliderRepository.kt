package com.workfort.pstuian.repository

import com.workfort.pstuian.database.service.SliderService
import com.workfort.pstuian.model.SliderEntity
import com.workfort.pstuian.networking.SliderApiHelper

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Sep, 2021 at 9:40 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class SliderRepository(
    private val dbService: SliderService,
    private val helper: com.workfort.pstuian.networking.SliderApiHelper,
) {
    suspend fun getSliders(forceRefresh: Boolean = false) : List<SliderEntity> {
        val existingData = if(forceRefresh) emptyList() else dbService.getAll()
        if(existingData.isEmpty()) {
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