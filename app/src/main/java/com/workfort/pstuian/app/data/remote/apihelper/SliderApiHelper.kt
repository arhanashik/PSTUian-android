package com.workfort.pstuian.app.data.remote.apihelper

import com.workfort.pstuian.app.data.local.slider.SliderEntity
import com.workfort.pstuian.util.remote.SliderApiService

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Sep, 2021 at 10:02 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1. The base activity of each activity
 *  * 2. Set layout, toolbar, onClickListeners for the activities
 *  * 3.
 *  *
 *  * Last edited by : arhan on 9/30/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class SliderApiHelper(private val service: SliderApiService) : ApiHelper<SliderEntity>() {
    override suspend fun getAll(): List<SliderEntity> {
        val response = service.getSliders()
        if(!response.success) throw Exception(response.message)

        return response.data?: emptyList()
    }
}