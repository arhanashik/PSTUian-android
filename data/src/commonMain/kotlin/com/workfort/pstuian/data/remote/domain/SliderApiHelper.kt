package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.dto.SliderDto
import com.workfort.pstuian.data.remote.service.SliderApiService

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

class SliderApiHelper(private val service: SliderApiService) : ApiHelper<SliderDto>() {
    override suspend fun getAll(): List<SliderDto> {
        val response = service.getSliders()
        if(!response.success) throw Exception(response.message)

        return response.data?: emptyList()
    }
}