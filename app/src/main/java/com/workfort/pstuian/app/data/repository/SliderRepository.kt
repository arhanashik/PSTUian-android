package com.workfort.pstuian.app.data.repository

import com.workfort.pstuian.app.data.local.slider.SliderEntity
import com.workfort.pstuian.app.data.local.slider.SliderService
import com.workfort.pstuian.app.data.remote.apihelper.SliderApiHelper

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Sep, 2021 at 9:40 PM.
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

class SliderRepository(
    private val dbService: SliderService,
    private val helper: SliderApiHelper
) {
    suspend fun getSliders(forceRefresh: Boolean = false) : List<SliderEntity> {
        val existingData = if(forceRefresh) emptyList() else dbService.getAll()
        if(existingData.isNullOrEmpty()) {
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