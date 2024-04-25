package com.workfort.pstuian.networking.domain

import com.workfort.pstuian.model.SliderEntity
import com.workfort.pstuian.networking.service.SliderApiService

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Sep, 2021 at 10:02 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class SliderApiHelper(private val service: SliderApiService) : ApiHelper<SliderEntity>() {
    override suspend fun getAll(): List<SliderEntity> {
        val response = service.getSliders()
        if(!response.success) throw Exception(response.message)

        return response.data?: emptyList()
    }
}